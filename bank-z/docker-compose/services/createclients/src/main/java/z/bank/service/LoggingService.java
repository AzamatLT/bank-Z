package z.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoggingService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.logs}")
    private String logsTopic;

    public void logToKafka(String level, String message) {
        String logMessage = String.format("[%s] %s - %s",
                level,
                LocalDateTime.now(),
                message);

        kafkaTemplate.send(logsTopic, logMessage);
    }
}
