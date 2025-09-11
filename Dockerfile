# ===== Build stage =====
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# 소스 복사
COPY mimidaily/src/main/java ./src/java
COPY mimidaily/src/main/webapp ./src/webapp

# 필요한 라이브러리 다운로드
RUN mkdir -p src/webapp/WEB-INF/lib \
 && curl -L -o src/webapp/WEB-INF/lib/commons-fileupload-1.5.jar \
      https://repo1.maven.org/maven2/commons-fileupload/commons-fileupload/1.5/commons-fileupload-1.5.jar \
 && curl -L -o src/webapp/WEB-INF/lib/commons-io-2.11.0.jar \
      https://repo1.maven.org/maven2/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar \
 && curl -L -o /tmp/javax.servlet-api-4.0.1.jar \
      https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar

# 자바 소스 컴파일
RUN mkdir -p src/webapp/WEB-INF/classes \
 && javac -cp /tmp/javax.servlet-api-4.0.1.jar:src/webapp/WEB-INF/lib/* \
          -d src/webapp/WEB-INF/classes \
          $(find src/java -name "*.java")

# ===== Runtime stage =====
FROM tomcat:9.0-jdk17-temurin

# Build stage에서 완성한 웹앱을 ROOT로 복사
COPY --from=build /app/src/webapp/ /usr/local/tomcat/webapps/ROOT/

EXPOSE 8080
CMD ["catalina.sh", "run"]