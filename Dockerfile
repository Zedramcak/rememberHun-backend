# Use official Java image as the base image
FROM eclipse-temurin:21-jdk-jammy as build

# Set working directory
WORKDIR /app

# Copy maven executables
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this step will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Create the final image with minimal dependencies
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/rememberHun-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (default Spring Boot port)
EXPOSE 8080

# Set up environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]