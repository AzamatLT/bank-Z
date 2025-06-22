package z.bank.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import z.bank.config.StubProperties;
import z.bank.service.KafkaLoggingService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/validate_people")
public class GosStubController {

    private final StubProperties stubProperties;
    private final KafkaLoggingService kafkaLoggingService;

    @Autowired
    public GosStubController(StubProperties stubProperties,
                             KafkaLoggingService kafkaLoggingService) {
        this.stubProperties = stubProperties;
        this.kafkaLoggingService = kafkaLoggingService;
    }

    /**
     * Основной endpoint с динамической задержкой
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> validate(@RequestParam String inn) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // Применяем задержку
        Thread.sleep(stubProperties.getResponseDelay());

        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Validation successful");

        long processingTime = System.currentTimeMillis() - startTime;

        // Логируем в Kafka
        kafkaLoggingService.logMockRequest(
                "GET /api/validate_people",
                response.toString(), // Преобразуем Map в String для логирования
                processingTime,
                stubProperties.getResponseDelay()
        );

        log.info("Processed request in {} ms (delay: {} ms)",
                processingTime, stubProperties.getResponseDelay());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Endpoint для динамического изменения задержки
     */
    @PostMapping("/config/delay")
    public ResponseEntity<String> updateDelay(@RequestParam int delay) {
        stubProperties.setResponseDelay(delay);
        kafkaLoggingService.logMockConfigChange(
                "Response delay changed to: " + delay + " ms"
        );
        return ResponseEntity.ok("Delay set to " + delay + " ms");
    }
}