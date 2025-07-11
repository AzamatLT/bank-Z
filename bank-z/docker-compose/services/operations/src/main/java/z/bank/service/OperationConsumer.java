package z.bank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import z.bank.dto.OperationMessage;

@Slf4j
@Service
public class OperationConsumer {
    private final ObjectMapper objectMapper;
    private final OperationProcessor operationProcessor;

    public OperationConsumer(ObjectMapper objectMapper, OperationProcessor operationProcessor) {
        this.objectMapper = objectMapper;
        this.operationProcessor = operationProcessor;
    }

    @KafkaListener(topics = "${app.topics.operations}")
    public void consumeOperation(String message) {
        try {
            OperationMessage operation = objectMapper.readValue(message, OperationMessage.class);
            log.info("Received operation: {}", operation);
            operationProcessor.processOperation(operation);
        } catch (JsonProcessingException e) {
            log.error("Error parsing operation message: {}", e.getMessage());
        }
    }
}
