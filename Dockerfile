# --- Build stage (javac) ---
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy sources and web resources
COPY mimidaily/src/main/java ./src/main/java
COPY mimidaily/src/main/webapp ./src/main/webapp

# Get Servlet API for compilation (Tomcat 9 uses javax.servlet 4.0)
RUN curl -fsSL -o /tmp/javax.servlet-api-4.0.1.jar https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar \
 && mkdir -p /app/WEB-INF/classes \
 && find src/main/java -name "*.java" > /tmp/sources.txt \
 && javac -encoding UTF-8 -cp /tmp/javax.servlet-api-4.0.1.jar -d /app/WEB-INF/classes @/tmp/sources.txt

# --- Runtime stage (Tomcat) ---
FROM tomcat:9.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*

# JDBC Driver (if not packaged inside the app)
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar /usr/local/tomcat/lib/mysql-connector-j.jar

# Copy webapp resources (JSP, WEB-INF, META-INF, static resources)
COPY --from=build /app/src/main/webapp/ /usr/local/tomcat/webapps/ROOT/

# Copy compiled servlet classes into WEB-INF/classes
COPY --from=build /app/WEB-INF/classes /usr/local/tomcat/webapps/ROOT/WEB-INF/classes

EXPOSE 8080
CMD ["catalina.sh","run"]