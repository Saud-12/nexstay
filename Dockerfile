FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/nexstay-0.0.1-SNAPSHOT.jar /app/nexstay.jar

CMD ["java", "-jar", "nexstay.jar"]