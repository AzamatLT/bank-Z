package z.bank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class KafkaLoggingService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.mock_logs}")
    private String mockLogsTopic;

    public KafkaLoggingService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void logMockRequest(String endpoint, String response,
                               long processingTime, int configuredDelay) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("endpoint", endpoint);
        logEntry.put("response", response);
        logEntry.put("processingTime", processingTime);
        logEntry.put("configuredDelay", configuredDelay);
        logEntry.put("type", "REQUEST_LOG");

        kafkaTemplate.send(mockLogsTopic, logEntry);
    }

    public void logMockConfigChange(String message) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("message", message);
        logEntry.put("type", "CONFIG_CHANGE");

        kafkaTemplate.send(mockLogsTopic, logEntry);
    }
}