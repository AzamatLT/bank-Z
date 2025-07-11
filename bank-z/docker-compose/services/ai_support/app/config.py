from pydantic import BaseSettings

class Settings(BaseSettings):
    # Настройки PostgreSQL
    postgres_url: str = "postgresql://postgres:postgres@postgres:5432/bank_ai"
    
    # Настройки Kafka
    kafka_bootstrap_servers: str = "kafka:9092"
    kafka_topic: str = "bank_conversations"
    
    # Порт FastAPI
    service_port: int = 48091
    
    # Порт Prometheus
    prometheus_port: int = 8000

# Создаём объект настроек
settings = Settings()