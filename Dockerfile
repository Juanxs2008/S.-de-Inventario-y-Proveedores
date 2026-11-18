
FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /app


RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://nodesource.com | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .


RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -Pproduction -DskipTests


FROM eclipse-temurin:25-jre-jammy
WORKDIR /app


RUN useradd --system appuser && chown -R appuser /app
USER appuser


COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080


ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]
