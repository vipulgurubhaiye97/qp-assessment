FROM openjdk:17-jdk-slim
LABEL authors="Vipul"
VOLUME /tmp
COPY target/grosery-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8081