package z.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import z.bank.config.StubProperties;
import z.bank.service.KafkaLoggingService;

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
    public ResponseEntity<String> handleGetRequest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // Применяем задержку
        Thread.sleep(stubProperties.getResponseDelay());

        String response = "OK";
        long processingTime = System.currentTimeMillis() - startTime;

        // Логируем в Kafka
        kafkaLoggingService.logMockRequest(
                "GET /api/validate_people",
                response,
                processingTime,
                stubProperties.getResponseDelay()
        );

        log.info("Processed request in {} ms (delay: {} ms)",
                processingTime, stubProperties.getResponseDelay());

        return ResponseEntity.ok(response);
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
