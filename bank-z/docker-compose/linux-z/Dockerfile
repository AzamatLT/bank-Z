FROM ubuntu:22.04

# Установка базовых пакетов
RUN apt-get update && apt-get install -y \
    sudo \
    curl \
    wget \
    python3 \
    python3-pip \
    openjdk-11-jdk \
    && rm -rf /var/lib/apt/lists/*

# Установка Ansible
RUN pip3 install ansible

# Установка JMeter 5.6.2
ARG JMETER_VERSION=5.6.2
RUN wget -O /tmp/apache-jmeter-${JMETER_VERSION}.tgz \
    https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-${JMETER_VERSION}.tgz \
    && tar -xzf /tmp/apache-jmeter-${JMETER_VERSION}.tgz -C /opt \
    && ln -s /opt/apache-jmeter-${JMETER_VERSION}/bin/jmeter /usr/local/bin/jmeter \
    && rm /tmp/apache-jmeter-${JMETER_VERSION}.tgz

# Настройка рабочей директории
WORKDIR /workspace

# Стандартная команда
CMD ["/bin/bash"]