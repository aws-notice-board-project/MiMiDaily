package com.mimidaily.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

/**
 * S3 업로드/삭제를 AWS SDK 없이 SigV4로 수행.
 * 방법 B: IRSA(WebIdentity)가 보이면 무조건 IRSA 우선, 정적 키는 유효성 통과 시에만 Fallback.
 */
public class S3StorageService {

    private static final DateTimeFormatter AMZ_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'", Locale.US).withZone(ZoneOffset.UTC);
    private static final String SERVICE = "s3";
    private static final String EMPTY_PAYLOAD_HASH =
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    private final String bucketName;
    private final String region;
    private final String publicBaseUrl;
    private final String endpoint;

    private final CredentialsProvider credentialsProvider;
    private final Object credentialsLock = new Object();
    private volatile AwsCredentials cachedCredentials;

    public S3StorageService(ServletContext context) {
        this.bucketName = requireConfig(context, "AWS_S3_BUCKET", "aws.s3.bucket");
        this.region = coalesce(
                optionalConfig(context, "AWS_S3_REGION", "aws.s3.region"),
                optionalConfig(context, "AWS_REGION", "aws.region"),
                optionalConfig(context, "AWS_DEFAULT_REGION", "aws.defaultRegion"),
                "ap-northeast-2"
        );

        String baseUrl = optionalConfig(context, "AWS_S3_BUCKET", "aws.s3.bucket");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
        } else if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        this.publicBaseUrl = baseUrl;
        this.endpoint = "https://" + bucketName + ".s3." + region + ".amazonaws.com";

        // --- 방법 B: IRSA 우선 → 정적 키는 유효성 통과 시에만 사용 ---
        WebIdentityConfiguration webIdentity = WebIdentityConfiguration.from(context);
        AwsCredentials staticCreds = loadStaticCredentials(context);

        if (webIdentity != null) {
            // IRSA가 보이면 무조건 IRSA
            this.credentialsProvider = new WebIdentityCredentialsProvider(webIdentity);
        } else if (isValidStatic(staticCreds)) {
            // IRSA 없고, 정적 키가 "정말로" 유효할 때만 fallback
            this.credentialsProvider = new StaticCredentialsProvider(staticCreds);
            this.cachedCredentials = staticCreds;
        } else {
            throw new IllegalStateException(
                    "No valid AWS credentials. IRSA(AWS_ROLE_ARN/AWS_WEB_IDENTITY_TOKEN_FILE) or valid static keys required.");
        }
    }

    /** 업로드 */
    public UploadResult upload(Part part, String keyPrefix) throws IOException {
        if (part == null || part.getSize() <= 0) return null;

        String originalFileName = sanitizeFileName(part.getSubmittedFileName());
        String extension = extractExtension(originalFileName, part.getContentType());
        String prefix = normalizePrefix(keyPrefix);
        String key = prefix + UUID.randomUUID().toString().replace("-", "") + extension;
        key = normalizeObjectKey(key);

        byte[] payload = readAllBytes(part.getInputStream());
        String contentType = (part.getContentType() != null && !part.getContentType().isEmpty())
                ? part.getContentType()
                : "application/octet-stream";

        executeRequest("PUT", key, payload, contentType);
        return new UploadResult(originalFileName, key, publicBaseUrl, payload.length, contentType);
    }

    /** 삭제 */
    public void delete(String objectKey) throws IOException {
        if (objectKey == null || objectKey.trim().isEmpty()) return;
        executeRequest("DELETE", normalizeObjectKey(objectKey.trim()), new byte[0], null);
    }

    // ================= core http =================
    private void executeRequest(String method, String objectKey, byte[] payload, String contentType) throws IOException {
        AwsCredentials credentials = getCredentials();
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
        if (credentials.sessionToken != null && !credentials.sessionToken.isEmpty()) {
            canonicalHeaders.put("x-amz-security-token", credentials.sessionToken);
        }
        if (contentType != null && !contentType.trim().isEmpty() && ("PUT".equals(method) || "POST".equals(method))) {
            canonicalHeaders.put("content-type", contentType.trim());
        }

        StringBuilder canonicalHeadersBuilder = new StringBuilder();
        StringBuilder signedHeadersBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> e : canonicalHeaders.entrySet()) {
            canonicalHeadersBuilder.append(e.getKey()).append(":").append(e.getValue().trim()).append("\n");
            if (!first) signedHeadersBuilder.append(";");
            signedHeadersBuilder.append(e.getKey());
            first = false;
        }
        String signedHeaders = signedHeadersBuilder.toString();
        String canonicalRequest = method + "\n" + canonicalUri + "\n\n" +
                canonicalHeadersBuilder + "\n" + signedHeaders + "\n" + payloadHash;

        String credentialScope = dateStamp + "/" + region + "/" + SERVICE + "/aws4_request";
        String stringToSign = "AWS4-HMAC-SHA256\n" + amzDate + "\n" + credentialScope + "\n" +
                hashHex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        byte[] signingKey = getSignatureKey(credentials.secretAccessKey, dateStamp, region, SERVICE);
        String signature;
        try {
            signature = toHex(hmacSha256(signingKey, stringToSign.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IOException("Failed to sign S3 request", e);
        }
        String authorizationHeader = "AWS4-HMAC-SHA256 Credential=" + credentials.accessKeyId + "/" + credentialScope +
                ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;

        URL url = new URL(endpoint + canonicalUri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        if ("PUT".equals(method) || "POST".equals(method)) conn.setDoOutput(true);

        for (Map.Entry<String, String> entry : canonicalHeaders.entrySet()) {
            conn.setRequestProperty(formatHeaderName(entry.getKey()), entry.getValue());
        }
        conn.setRequestProperty("Authorization", authorizationHeader);

        if (payload.length > 0) {
            conn.setFixedLengthStreamingMode(payload.length);
            try (OutputStream out = conn.getOutputStream()) {
                out.write(payload);
            }
        }

        int code = conn.getResponseCode();
        if (code < 200 || code >= 300) {
            String body = safeReadUtf8(conn.getErrorStream());
            conn.disconnect();
            // 디버깅에 도움 되도록 표준에러로도 남김
            System.err.println("[S3][DEBUG] HTTP " + code + " " + method + " " + endpoint + canonicalUri);
            System.err.println("[S3][DEBUG] ERROR BODY: " + body);
            throw new IOException("S3 request failed " + code + " - " + body);
        }
        conn.disconnect();
    }

    private AwsCredentials getCredentials() throws IOException {
        AwsCredentials creds = cachedCredentials;
        if (creds == null || creds.isExpired()) {
            synchronized (credentialsLock) {
                creds = cachedCredentials;
                if (creds == null || creds.isExpired()) {
                    creds = credentialsProvider.resolve();
                    if (creds == null) throw new IOException("Unable to load AWS credentials");
                    cachedCredentials = creds;
                }
            }
        }
        return creds;
    }

    // ================= helpers =================
    private static boolean isValidStatic(AwsCredentials c) {
        if (c == null) return false;
        // 템플릿/플레이스홀더("${...}") 또는 공백 검출
        if (looksLikePlaceholder(c.accessKeyId) || looksLikePlaceholder(c.secretAccessKey)) return false;
        // 일반적인 AccessKeyId 패턴: AKIA/ASIA/ANPA 로 시작 + 총 20자 (대략 검증)
        return c.accessKeyId != null && c.accessKeyId.matches("^(AKIA|ASIA|ANPA)[A-Z0-9]{16}$")
                && c.secretAccessKey != null && c.secretAccessKey.length() >= 30;
    }

    private static boolean looksLikePlaceholder(String s) {
        if (s == null) return false;
        String t = s.trim();
        return t.isEmpty() || t.startsWith("${") || t.contains("AWS_ACCESS_KEY_ID") || t.contains("AWS_SECRET_ACCESS_KEY");
    }

    private static String normalizeObjectKey(String key) {
        if (key == null) return "";
        while (key.startsWith("/")) key = key.substring(1);
        return key;
    }

    private static String normalizePrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) return "";
        String p = prefix.trim();
        if (p.startsWith("/")) p = p.substring(1);
        if (!p.endsWith("/")) p = p + "/";
        return p;
    }

    private static String sanitizeFileName(String name) {
        if (name == null) return null;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0 && slash + 1 < name.length()) return name.substring(slash + 1);
        return name;
    }

    private static String extractExtension(String original, String contentType) {
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot > -1 && dot < original.length() - 1) return original.substring(dot);
        }
        if (contentType != null) {
            if ("image/jpeg".equals(contentType)) return ".jpg";
            if ("image/png".equals(contentType)) return ".png";
            if ("image/gif".equals(contentType)) return ".gif";
        }
        return "";
    }

    private static String uriEncode(String input, boolean encodeSlash) {
        StringBuilder result = new StringBuilder();
        for (byte b : input.getBytes(StandardCharsets.UTF_8)) {
            char c = (char) b;
            boolean unreserved = (c >= 'A' && c <= 'Z') ||
                    (c >= 'a' && c <= 'z') ||
                    (c >= '0' && c <= '9') ||
                    c == '-' || c == '_' || c == '.' || c == '~';
            if (unreserved || (!encodeSlash && c == '/')) {
                result.append((char) b);
            } else {
                result.append(String.format("%%%02X", b));
            }
        }
        return result.toString();
    }

    private static String formatHeaderName(String name) {
        if (Objects.equals("host", name)) return "Host";
        if (Objects.equals("content-type", name)) return "Content-Type";
        if (Objects.equals("x-amz-content-sha256", name)) return "x-amz-content-sha256";
        if (Objects.equals("x-amz-date", name)) return "x-amz-date";
        if (Objects.equals("x-amz-security-token", name)) return "X-Amz-Security-Token";
        return name;
    }

    private static String hashHex(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return toHex(md.digest());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] hmacSha256(byte[] key, byte[] data) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data);
    }

    private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) {
        try {
            byte[] kSecret = ("AWS4" + key).getBytes(StandardCharsets.UTF_8);
            byte[] kDate = hmacSha256(kSecret, dateStamp.getBytes(StandardCharsets.UTF_8));
            byte[] kRegion = hmacSha256(kDate, regionName.getBytes(StandardCharsets.UTF_8));
            byte[] kService = hmacSha256(kRegion, serviceName.getBytes(StandardCharsets.UTF_8));
            return hmacSha256(kService, "aws4_request".getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        try (InputStream i = in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = i.read(buf)) != -1) out.write(buf, 0, r);
            return out.toByteArray();
        }
    }

    private static String safeReadUtf8(InputStream stream) {
        if (stream == null) return "";
        try (InputStream in = stream; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int n;
            while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private static AwsCredentials loadStaticCredentials(ServletContext context) {
        String accessKey = optionalConfig(context, "AWS_ACCESS_KEY_ID", "aws.accessKeyId");
        String secretKey = optionalConfig(context, "AWS_SECRET_ACCESS_KEY", "aws.secretAccessKey");
        if (accessKey == null || secretKey == null) return null;
        String sessionToken = optionalConfig(context, "AWS_SESSION_TOKEN", "aws.sessionToken");
        return new AwsCredentials(accessKey.trim(), secretKey.trim(),
                sessionToken != null ? sessionToken.trim() : null, null);
    }

    private static String requireConfig(ServletContext context, String env, String param) {
        String v = optionalConfig(context, env, param);
        if (v == null || v.trim().isEmpty())
            throw new IllegalStateException("Missing S3 configuration for " + env + " or context-param " + param);
        return v.trim();
    }

    private static String optionalConfig(ServletContext context, String env, String param) {
        String ev = System.getenv(env);
        if (ev != null && !ev.trim().isEmpty()) return ev.trim();
        if (context != null) {
            String pv = context.getInitParameter(param);
            if (pv != null && !pv.trim().isEmpty()) return pv.trim();
        }
        return null;
    }

    private static String coalesce(String... vals) {
        for (String v : vals) if (v != null && !v.trim().isEmpty()) return v.trim();
        return null;
    }

    // ===== Credentials =====
    private interface CredentialsProvider { AwsCredentials resolve() throws IOException; }

    private static final class AwsCredentials {
        final String accessKeyId;
        final String secretAccessKey;
        final String sessionToken;
        final Instant expiration;

        AwsCredentials(String accessKeyId, String secretAccessKey, String sessionToken, Instant expiration) {
            this.accessKeyId = accessKeyId;
            this.secretAccessKey = secretAccessKey;
            this.sessionToken = sessionToken;
            this.expiration = expiration;
        }

        boolean isExpired() {
            if (expiration == null) return false;
            return Instant.now().isAfter(expiration.minus(Duration.ofMinutes(1)));
        }
    }

    private static final class StaticCredentialsProvider implements CredentialsProvider {
        private final AwsCredentials creds;
        StaticCredentialsProvider(AwsCredentials c) { this.creds = c; }
        public AwsCredentials resolve() { return creds; }
    }

    private static final class WebIdentityConfiguration {
        final String tokenFile;
        final String roleArn;
        final String roleSessionName;
        final String stsRegion;

        private WebIdentityConfiguration(String tokenFile, String roleArn, String roleSessionName, String stsRegion) {
            this.tokenFile = tokenFile;
            this.roleArn = roleArn;
            this.roleSessionName = roleSessionName;
            this.stsRegion = stsRegion;
        }

        static WebIdentityConfiguration from(ServletContext ctx) {
            String tokenFile = optionalConfig(ctx, "AWS_WEB_IDENTITY_TOKEN_FILE", "aws.webIdentityTokenFile");
            String roleArn = optionalConfig(ctx, "AWS_ROLE_ARN", "aws.roleArn");
            if (tokenFile == null || roleArn == null) return null;

            String sessionName = optionalConfig(ctx, "AWS_ROLE_SESSION_NAME", "aws.roleSessionName");
            if (sessionName == null || sessionName.isEmpty())
                sessionName = "mimidaily-" + UUID.randomUUID().toString().replace("-", "");

            String stsRegion = coalesce(
                    optionalConfig(ctx, "AWS_STS_REGION", "aws.sts.region"),
                    optionalConfig(ctx, "AWS_REGION", "aws.region"),
                    optionalConfig(ctx, "AWS_DEFAULT_REGION", "aws.defaultRegion")
            );
            return new WebIdentityConfiguration(tokenFile, roleArn, sessionName, stsRegion);
        }

        String endpoint() {
            if (stsRegion == null || stsRegion.isEmpty()) return "https://sts.amazonaws.com";
            return "https://sts." + stsRegion + ".amazonaws.com";
        }
    }

    private static final class WebIdentityCredentialsProvider implements CredentialsProvider {
        private final WebIdentityConfiguration cfg;
        WebIdentityCredentialsProvider(WebIdentityConfiguration c) { this.cfg = c; }

        public AwsCredentials resolve() throws IOException {
            String token = new String(Files.readAllBytes(Paths.get(cfg.tokenFile)), StandardCharsets.UTF_8).trim();
            if (token.isEmpty()) throw new IOException("Web identity token is empty: " + cfg.tokenFile);

            String body = "Action=AssumeRoleWithWebIdentity&Version=2011-06-15" +
                    "&RoleArn=" + URLEncoder.encode(cfg.roleArn, StandardCharsets.UTF_8.name()) +
                    "&RoleSessionName=" + URLEncoder.encode(cfg.roleSessionName, StandardCharsets.UTF_8.name()) +
                    "&WebIdentityToken=" + URLEncoder.encode(token, StandardCharsets.UTF_8.name());

            byte[] payload = body.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(cfg.endpoint());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setFixedLengthStreamingMode(payload.length);
            try (OutputStream out = conn.getOutputStream()) {
                out.write(payload);
            }
            int status = conn.getResponseCode();
            String resp = status >= 200 && status < 300 ? safeReadUtf8(conn.getInputStream()) : safeReadUtf8(conn.getErrorStream());
            conn.disconnect();
            if (status < 200 || status >= 300) {
                throw new IOException("STS AssumeRoleWithWebIdentity failed " + status + " - " + resp);
            }
            return parseStsResponse(resp);
        }
    }

    private static AwsCredentials parseStsResponse(String xml) throws IOException {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            f.setExpandEntityReferences(false);

            Document doc = f.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            NodeList creds = doc.getElementsByTagName("Credentials");
            if (creds.getLength() == 0) throw new IOException("STS response missing <Credentials>");
            Element c = (Element) creds.item(0);
            String accessKeyId = getTag(c, "AccessKeyId");
            String secret = getTag(c, "SecretAccessKey");
            String token = getTag(c, "SessionToken");
            String exp = getTag(c, "Expiration");
            if (accessKeyId == null || secret == null || token == null) {
                throw new IOException("STS returned incomplete credentials");
            }
            Instant expiration = (exp != null && !exp.isEmpty()) ? Instant.parse(exp.trim()) : null;
            return new AwsCredentials(accessKeyId.trim(), secret.trim(), token.trim(), expiration);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Unable to parse STS response", e);
        }
    }

    private static String getTag(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        Node n = nodes.item(0);
        return n == null ? null : n.getTextContent();
    }

    // ===== DTO =====
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

        public String getOriginalFileName() { return originalFileName; }
        public String getObjectKey() { return objectKey; }
        public String getBasePath() { return basePath; }
        public long getSize() { return size; }
        public String getContentType() { return contentType; }
    }
}