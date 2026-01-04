# -------- Build image --------
FROM eclipse-temurin:21-jre-jammy

# Security: run as non-root
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /app

# Copy the Spring Boot JAR
COPY target/knowledge-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# JVM optimizations for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
