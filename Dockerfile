FROM amazoncorretto:17-alpine

# Set working directory
WORKDIR /app

# Copy the fat JAR into the container
COPY target/*.jar app.jar

# Expose port (only for local testing, not used in Lambda)
EXPOSE 8080

# Lambda requires this entrypoint
CMD ["java", "-jar", "app.jar"]