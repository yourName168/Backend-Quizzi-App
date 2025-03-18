# place to build the image
# step 1: build

FROM maven:3.9.8-amazoncorretto-21 as build

# copy the source code to the image
WORKDIR /app
COPY pom.xml .
COPY src ./src

# build the application
RUN mvn clean 
RUN mvn package -DskipTests

# step 2: run
FROM amazoncorretto:21.0.4

# copy the jar file to the image
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# run the application]
ENTRYPOINT [ "java", "-jar", "app.jar", "-DskipTests" ]