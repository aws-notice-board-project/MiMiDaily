package com.mimidaily.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

/**
 * Utility class that uploads and deletes files from Amazon S3 without relying on the AWS SDK.
 * The implementation uses the official Signature Version 4 signing process to perform HTTP calls
 * directly so that the legacy project can integrate with S3 without introducing a build tool.
 */
public class S3StorageService {

    private static final DateTimeFormatter AMZ_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'", Locale.US).withZone(ZoneOffset.UTC);
    private static final String SERVICE = "s3";
    private static final String EMPTY_PAYLOAD_HASH =
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    private final String bucketName;
    private final String region;
    private final String accessKey;
    private final String secretKey;
    private final String sessionToken;
    private final String publicBaseUrl;
    private final String endpoint;

    public S3StorageService(ServletContext context) {
        this.bucketName = requireConfig(context, "AWS_S3_BUCKET", "aws.s3.bucket");
        this.region = requireConfig(context, "AWS_S3_REGION", "aws.s3.region");
        this.accessKey = requireConfig(context, "AWS_ACCESS_KEY_ID", "aws.accessKeyId");
        this.secretKey = requireConfig(context, "AWS_SECRET_ACCESS_KEY", "aws.secretAccessKey");
        this.sessionToken = optionalConfig(context, "AWS_SESSION_TOKEN", "aws.sessionToken");

        String baseUrl = optionalConfig(context, "AWS_S3_PUBLIC_URL", "aws.s3.publicUrl");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
        } else if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        this.publicBaseUrl = baseUrl;
        this.endpoint = "https://" + bucketName + ".s3." + region + ".amazonaws.com";
    }

    public UploadResult upload(Part part, String keyPrefix) throws IOException {
        if (part == null || part.getSize() <= 0) {
            return null;
        }
        String originalFileName = sanitizeFileName(part.getSubmittedFileName());
        String extension = extractExtension(originalFileName, part.getContentType());
        String prefix = normalizePrefix(keyPrefix);
        String key = prefix + UUID.randomUUID().toString().replace("-", "") + extension;

        byte[] payload = readAllBytes(part.getInputStream());
        executeRequest("PUT", key, payload, part.getContentType());

        return new UploadResult(originalFileName, key, publicBaseUrl, payload.length, part.getContentType());
    }

    public void delete(String objectKey) throws IOException {
        if (objectKey == null || objectKey.trim().isEmpty()) {
            return;
        }
        executeRequest("DELETE", objectKey.trim(), new byte[0], null);
    }

    private void executeRequest(String method, String objectKey, byte[] payload, String contentType) throws IOException {
        String canonicalKey = uriEncode(objectKey, false);
        String canonicalUri = "/" + canonicalKey;

        String amzDate = AMZ_DATE_FORMAT.format(Instant.now());
        String dateStamp = amzDate.substring(0, 8);
        String payloadHash = payload.length == 0 ? EMPTY_PAYLOAD_HASH : hashHex(payload);

        Map<String, String> canonicalHeaders = new TreeMap<>();
        String hostHeader = bucketName + ".s3." + region + ".amazonaws.com";
        canonicalHeaders.put("host", hostHeader);
        canonicalHeaders.put("x-amz-content-sha256", payloadHash);
        canonicalHeaders.put("x-amz-date", amzDate);
        if (sessionToken != null && !sessionToken.trim().isEmpty()) {
            canonicalHeaders.put("x-amz-security-token", sessionToken.trim());
        }
        if (contentType != null && !contentType.trim().isEmpty() && ("PUT".equals(method) || "POST".equals(method))) {
            canonicalHeaders.put("content-type", contentType.trim());
        }

        StringBuilder canonicalHeadersBuilder = new StringBuilder();
        StringBuilder signedHeadersBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : canonicalHeaders.entrySet()) {
            canonicalHeadersBuilder.append(entry.getKey()).append(":").append(entry.getValue().trim()).append("\n");
            if (!first) {
                signedHeadersBuilder.append(";");
            }
            signedHeadersBuilder.append(entry.getKey());
            first = false;
        }
        String signedHeaders = signedHeadersBuilder.toString();
        String canonicalRequest = method + "\n" + canonicalUri + "\n\n" + canonicalHeadersBuilder + "\n" + signedHeaders + "\n" + payloadHash;
        String credentialScope = dateStamp + "/" + region + "/" + SERVICE + "/aws4_request";
        String stringToSign = "AWS4-HMAC-SHA256\n" + amzDate + "\n" + credentialScope + "\n" + hashHex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        byte[] signingKey = getSignatureKey(secretKey, dateStamp, region, SERVICE);
        String signature;
        try {
            signature = toHex(hmacSha256(signingKey, stringToSign.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to sign S3 request", e);
        }
        String authorizationHeader = "AWS4-HMAC-SHA256 Credential=" + accessKey + "/" + credentialScope +
            ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;

        URL url = new URL(endpoint + canonicalUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if ("PUT".equals(method) || "POST".equals(method)) {
            connection.setDoOutput(true);
        }

        for (Map.Entry<String, String> entry : canonicalHeaders.entrySet()) {
            connection.setRequestProperty(formatHeaderName(entry.getKey()), entry.getValue());
        }
        connection.setRequestProperty("Authorization", authorizationHeader);

        if (payload.length > 0) {
            connection.setFixedLengthStreamingMode(payload.length);
            try (OutputStream out = connection.getOutputStream()) {
                out.write(payload);
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            String errorMessage = readError(connection);
            throw new IOException("S3 request failed with status " + responseCode + ": " + errorMessage);
        }
        connection.disconnect();
    }

    private static String readError(HttpURLConnection connection) {
        InputStream errorStream = connection.getErrorStream();
        if (errorStream == null) {
            return "";
        }
        try (InputStream in = errorStream; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private static String sanitizeFileName(String submittedFileName) {
        if (submittedFileName == null) {
            return null;
        }
        int slashIndex = Math.max(submittedFileName.lastIndexOf('/'), submittedFileName.lastIndexOf('\\'));
        if (slashIndex >= 0 && slashIndex + 1 < submittedFileName.length()) {
            return submittedFileName.substring(slashIndex + 1);
        }
        return submittedFileName;
    }

    private static String extractExtension(String originalFileName, String contentType) {
        if (originalFileName != null) {
            int dotIdx = originalFileName.lastIndexOf('.');
            if (dotIdx > -1 && dotIdx < originalFileName.length() - 1) {
                return originalFileName.substring(dotIdx);
            }
        }
        if (contentType != null) {
            if (contentType.equals("image/jpeg")) {
                return ".jpg";
            } else if (contentType.equals("image/png")) {
                return ".png";
            } else if (contentType.equals("image/gif")) {
                return ".gif";
            } else if (contentType.equals("image/webp")) {
                return ".webp";
            }
        }
        return "";
    }

    private static String normalizePrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return "";
        }
        String cleaned = prefix.trim();
        while (cleaned.startsWith("/")) {
            cleaned = cleaned.substring(1);
        }
        while (cleaned.endsWith("/")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }
        return cleaned.isEmpty() ? "" : cleaned + "/";
    }

    private static byte[] readAllBytes(InputStream input) throws IOException {
        try (InputStream in = input; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        }
    }

    private static byte[] hmacSha256(byte[] key, byte[] data) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data);
    }

    private static byte[] getSignatureKey(String secretKey, String dateStamp, String regionName, String serviceName)
            throws IOException {
        try {
            byte[] kSecret = ("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8);
            byte[] kDate = hmacSha256(kSecret, dateStamp.getBytes(StandardCharsets.UTF_8));
            byte[] kRegion = hmacSha256(kDate, regionName.getBytes(StandardCharsets.UTF_8));
            byte[] kService = hmacSha256(kRegion, serviceName.getBytes(StandardCharsets.UTF_8));
            return hmacSha256(kService, "aws4_request".getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to derive signing key", e);
        }
    }

    private static String hashHex(byte[] data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            return toHex(digest);
        } catch (GeneralSecurityException e) {
            throw new IOException("Unable to calculate SHA-256 hash", e);
        }
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String uriEncode(String input, boolean encodeSlash) {
        StringBuilder result = new StringBuilder();
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            char ch = (char) b;
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                    || ch == '_' || ch == '-' || ch == '~' || ch == '.' ) {
                result.append((char) ch);
            } else if (ch == '/' && !encodeSlash) {
                result.append('/');
            } else {
                result.append(String.format("%%%02X", b));
            }
        }
        return result.toString();
    }

    private static String formatHeaderName(String canonicalName) {
        if (Objects.equals("host", canonicalName)) {
            return "Host";
        }
        if (Objects.equals("content-type", canonicalName)) {
            return "Content-Type";
        }
        if (Objects.equals("x-amz-content-sha256", canonicalName)) {
            return "x-amz-content-sha256";
        }
        if (Objects.equals("x-amz-date", canonicalName)) {
            return "x-amz-date";
        }
        if (Objects.equals("x-amz-security-token", canonicalName)) {
            return "X-Amz-Security-Token";
        }
        return canonicalName;
    }

    private static String requireConfig(ServletContext context, String envName, String paramName) {
        String value = optionalConfig(context, envName, paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing S3 configuration for " + envName + " or context-param " + paramName);
        }
        return value.trim();
    }

    private static String optionalConfig(ServletContext context, String envName, String paramName) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        if (context != null) {
            String paramValue = context.getInitParameter(paramName);
            if (paramValue != null && !paramValue.trim().isEmpty()) {
                return paramValue.trim();
            }
        }
        return null;
    }

    public static class UploadResult {
        private final String originalFileName;
        private final String objectKey;
        private final String basePath;
        private final long size;
        private final String contentType;

        public UploadResult(String originalFileName, String objectKey, String basePath, long size, String contentType) {
            this.originalFileName = originalFileName;
            this.objectKey = objectKey;
            this.basePath = basePath;
            this.size = size;
            this.contentType = contentType;
        }

        public String getOriginalFileName() {
            return originalFileName;
        }

        public String getObjectKey() {
            return objectKey;
        }

        public String getBasePath() {
            return basePath;
        }

        public long getSize() {
            return size;
        }

        public String getContentType() {
            return contentType;
        }

        public String getPublicUrl() {
            if (objectKey == null || objectKey.trim().isEmpty()) {
                return null;
            }
            if (basePath == null || basePath.trim().isEmpty()) {
                return objectKey;
            }
            if (basePath.endsWith("/")) {
                return basePath + objectKey;
            }
            return basePath + "/" + objectKey;
        }
    }
}

