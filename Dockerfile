# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy code and generate JAR skipping the tests
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a no-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only JAR from last stage
COPY --from=build /app/target/*.jar app.jar

# Expose the API port
EXPOSE 8080

# Command to start
ENTRYPOINT ["java", "-jar", "app.jar"]