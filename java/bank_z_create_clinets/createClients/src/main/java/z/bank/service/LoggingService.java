package z.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    @Value("${kafka.topic.logs}")
    private String logsTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void logToKafka(String level, String message) {
        String logMessage = String.format("[%s] %s", level, message);
        logger.info("Sending log to Kafka: {}", logMessage);
        kafkaTemplate.send(logsTopic, logMessage);
    }
}
