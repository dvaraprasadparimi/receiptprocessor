# define base docker image
FROM openjdk:23

# Copy the Spring Boot JAR file into the container
COPY target/processor-0.0.1-SNAPSHOT.jar processor.jar


# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "processor.jar"]
