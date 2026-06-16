# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Use Java 21 JDK
FROM eclipse-temurin:21-jdk-alpine

# Copy the built JAR
ARG JAR_FILE=target/movie-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]