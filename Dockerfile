FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package

FROM openjdk:17-oracle

COPY --from=build /app/target/xml-reader*.jar /opt/app/xml-reader.jar

WORKDIR /opt/app

ENTRYPOINT ["java", "-jar", "xml-reader.jar"]
