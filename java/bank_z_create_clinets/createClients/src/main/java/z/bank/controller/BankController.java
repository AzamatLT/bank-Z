package z.bank.controller;

import z.bank.dto.TransactionRequest;
import z.bank.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/createclients")
public class BankController {
    private final LoggingService loggingService;

    @Autowired
    public BankController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping
    public ResponseEntity<String> processTransaction(@RequestBody TransactionRequest request) {
        loggingService.logToKafka("INFO", "Received create clients: " + request.toString());

        // Здесь должна быть логика обработки транзакции
        // Пока просто возвращаем успешный ответ

        loggingService.logToKafka("INFO", "The client was created successfully");
        return ResponseEntity.ok("The client was created successfully");
    }
}
