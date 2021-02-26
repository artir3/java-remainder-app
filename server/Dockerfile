FROM maven:3-openjdk-11

EXPOSE 8080

WORKDIR /app

COPY src /app
COPY pom.xml /app
COPY mvnw /app

RUN mvn build