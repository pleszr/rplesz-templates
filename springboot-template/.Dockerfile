FROM maven:3-eclipse-temurin-21 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY .. /app/
# Build the application using Maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/YOURFOLDER/target/kemper*-exec.jar /app.jar
#VOLUME /tmp

ENV SAMPLE_ENV_VAR="sample"

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev", "/app.jar"]