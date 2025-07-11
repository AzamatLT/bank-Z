FROM ubuntu:22.04

# Установка базовых зависимостей
RUN apt-get update && apt-get install -y \
    sudo \
    curl \
    wget \
    git \
    openssh-client \
    python3 \
    python3-pip \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Установка Ansible
RUN pip3 install ansible

# Создаем рабочие директории
RUN mkdir -p /ansible/{shared_data,jmeter_scripts,k6_scripts,gatling_scripts,data}

# Копируем playbook
COPY setup.yml .

# Запускаем установку
RUN ansible-playbook setup.yml

WORKDIR /ansible/shared_data
CMD ["sleep", "infinity"]