FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/nexstay-0.0.1-SNAPSHOT.jar nexstay.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "nexstay.jar"]