FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the project directory
COPY ./build/libs/nexstay-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
