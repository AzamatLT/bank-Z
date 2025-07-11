from confluent_kafka import Producer
import json
from app.config import settings
import logging

logger = logging.getLogger(__name__)

class KafkaProducer:
    def __init__(self):
        self.conf = {
            'bootstrap.servers': settings.kafka_bootstrap_servers,
            'message.max.bytes': 5242880,  # 5MB
            'queue.buffering.max.messages': 100000
        }
        self.producer = Producer(self.conf)
        self.topic = settings.kafka_topic

    def delivery_report(self, err, msg):
        if err is not None:
            logger.error(f"Message delivery failed: {err}")
        else:
            logger.debug(f"Message delivered to {msg.topic()} [{msg.partition()}]")

    def send_message(self, key: str, value: dict):
        try:
            self.producer.produce(
                topic=self.topic,
                key=key,
                value=json.dumps(value),
                callback=self.delivery_report
            )
            self.producer.poll(0)
        except Exception as e:
            logger.error(f"Kafka produce error: {e}")

    def flush(self):
        self.producer.flush()

# Глобальный экземпляр продюсера
kafka_producer = KafkaProducer()