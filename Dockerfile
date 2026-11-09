FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://nodesource.com | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
COPY src ./src

RUN NODE_OPTIONS="--max-old-space-size=400" ./mvnw clean package -Pproduction -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --system appuser
USER appuser

COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "-Dserver.port=${PORT:8080}", "app.jar"]
