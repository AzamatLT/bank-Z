package z.bank.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaLoggingService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String logsTopic = "logs";

    public KafkaLoggingService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logOperation(String operation, Map<String, Object> details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("operation", operation);
        logEntry.putAll(details);

        kafkaTemplate.send(logsTopic, logEntry);
    }
}
