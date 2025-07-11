package z.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import z.bank.dto.LogMessage;
import z.bank.dto.NotificationMessage;
import z.bank.dto.OperationMessage;
import z.bank.model.*;
import z.bank.repository.*;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationProcessor {
    private final AccountRepository accountRepository;
    private final OperationHistoryRepository historyRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional(timeout = 30)
    public void processOperation(OperationMessage operation) {
        try {
            Account account = accountRepository.findByCardNumber(operation.getCardNumber())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Проверка валюты с логированием
            if (!account.getCurrencyId().equals(operation.getCurrencyCode())) {
                log.warn("Currency mismatch for account {}: expected {}, got {}",
                        account.getId(), account.getCurrencyId(), operation.getCurrencyCode());
                throw new RuntimeException("Currency mismatch");
            }

            // Операция пополнения
            if ("INCREASE".equals(operation.getOperationType())) {
                accountRepository.increaseBalance(operation.getCardNumber(), operation.getAmount());
            }
            // Операция списания
            else if ("DECREASE".equals(operation.getOperationType())) {
                int updated = accountRepository.decreaseBalance(
                        operation.getCardNumber(),
                        operation.getAmount()
                );
                if (updated == 0) {
                    throw new RuntimeException("Insufficient funds");
                }
            }

            // Быстрая запись в историю
            saveOperationHistory(operation, "SUCCESS");

        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
            saveOperationHistory(operation, "FAILED: " + e.getMessage());
            throw e;
        }
    }

    private void saveOperationHistory(OperationMessage operation, String status) {
        OperationHistory history = new OperationHistory();
        history.setCardNumber(operation.getCardNumber());
        history.setAmount(operation.getAmount());
        history.setOperationType(operation.getOperationType());
        history.setStatus(status);
        history.setOperationDate(LocalDateTime.now());

        historyRepository.save(history);
    }
}