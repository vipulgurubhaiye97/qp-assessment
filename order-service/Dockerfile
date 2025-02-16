FROM openjdk:17-jdk-slim
LABEL authors="Vipul"
VOLUME /tmp
COPY target/order-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8082