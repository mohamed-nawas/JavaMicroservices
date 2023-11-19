# Build stage
FROM maven:3.9.5-amazoncorretto-11 AS build-env
WORKDIR /App
# Copy pom.xml
COPY pom.xml .
# Go offline using pom.xml
RUN mvn dependency:go-offline
# Copy src files
COPY ./src ./src
# Compile and Package JAR
RUN mvn clean install

# Build runtime image
FROM openjdk:11-jre
WORKDIR /App
# Copy build artifact from maven image from build stage
COPY --from=build-env /App/target/discovery-0.0.1-SNAPSHOT.jar /App/app.jar

# Running application
EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "/App/app.jar" ]