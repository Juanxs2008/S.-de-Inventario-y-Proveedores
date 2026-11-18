# ---------- Etapa 1: build ----------
FROM eclipse-temurin:25-jdk-noble AS build
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -Pproduction -DskipTests -B

# ---------- Etapa 2: ejecución ----------
FROM eclipse-temurin:25-jre-noble
WORKDIR /app

RUN useradd --system --create-home appuser
COPY --from=build /app/target/*.jar app.jar
RUN chown appuser /app/app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75 -Dserver.port=${PORT:-8080} -jar app.jar"]