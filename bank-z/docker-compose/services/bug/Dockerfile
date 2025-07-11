FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Используем wildcard для копирования JAR-файла
COPY target/bug-1.0.jar app.jar

EXPOSE 48089
EXPOSE 1099

ENV JAVA_OPTS="\
    -Djava.rmi.server.hostname=localhost \
    -Dcom.sun.management.jmxremote \
    -Dcom.sun.management.jmxremote.port=1099 \
    -Dcom.sun.management.jmxremote.rmi.port=1099 \
    -Dcom.sun.management.jmxremote.local.only=false \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false"

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]