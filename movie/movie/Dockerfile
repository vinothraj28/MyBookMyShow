version: "3.9"
services:
  db:
    image: postgres:15
    container_name: postgres-db
    environment:
      POSTGRES_DB: movie
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  backend:
    image: eclipse-temurin:21-jdk-alpine
    container_name: movie-backend
    depends_on:
      - db
    working_dir: /app
    volumes:
      - ./target/movie.jar:/app/app.jar   # map your built JAR
    command: ["java", "-jar", "app.jar"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/movie
      SPRING_DATASOURCE_USERNAME: myuser
      SPRING_DATASOURCE_PASSWORD: mypassword
    ports:
      - "8080:8080"

volumes:
  postgres-data:
