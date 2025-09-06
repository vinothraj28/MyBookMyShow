# Use Java 21 JDK
FROM eclipse-temurin:21-jdk-alpine

# Copy the built JAR
ARG JAR_FILE=target/movie-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]