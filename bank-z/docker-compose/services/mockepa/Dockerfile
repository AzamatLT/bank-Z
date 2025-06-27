# Build stage
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app

# Копируем только POM сначала (для кэширования зависимостей)
COPY pom.xml .
# Скачиваем все зависимости
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src ./src
# Собираем проект
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Настройки для мониторинга
ENV SERVICE_COLOR="orange" \
    SERVICE_TYPE="mock" \
	JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 \
               -Dmanagement.endpoints.web.exposure.include=health,metrics,prometheus \
               -Dmanagement.metrics.export.prometheus.enabled=true"
EXPOSE 48081

RUN useradd -m appuser && chown appuser:appuser /app
USER appuser

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]