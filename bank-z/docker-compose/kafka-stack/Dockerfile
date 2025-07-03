FROM confluentinc/cp-kafka:7.4.0

# Устанавливаем JMX Exporter
USER root
RUN mkdir -p /opt/jmx_exporter && \
    wget -O /opt/jmx_exporter/jmx_prometheus_javaagent.jar \
    https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar

# Копируем конфиг JMX Exporter
COPY jmx-config.yml /opt/jmx_exporter/jmx-config.yml

USER appuser