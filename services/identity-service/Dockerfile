# place to build the image
# step 1: build

FROM maven:3.8.5-openjdk-17-slim AS build

# copy the source code to the image
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

# build the application
RUN mvn package -DskipTests

# step 2: run
FROM openjdk:17-slim

# copy the jar file to the image
WORKDIR /app
COPY --from=build /app/target/*.jar identity-service.jar
EXPOSE 8085

# run the application
ENTRYPOINT ["java", "-jar", "identity-service.jar"]