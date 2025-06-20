FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Настройки для мониторинга
ENV SERVICE_COLOR="orange" \
    SERVICE_TYPE="mock" \
	JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 \
               -Dmanagement.endpoints.web.exposure.include=health,metrics,prometheus \
               -Dmanagement.metrics.export.prometheus.enabled=true"
EXPOSE 8080 8081  # 8081 для actuator (если нужно)

RUN useradd -m appuser && chown appuser:appuser /app
USER appuser

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]