FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/VerveAssignment-0.0.1-SNAPSHOT.jar ./app.jar
COPY --from=build /app/src/main/resources/promotions.csv ./

EXPOSE 1321

CMD ["java", "-jar", "app.jar"]