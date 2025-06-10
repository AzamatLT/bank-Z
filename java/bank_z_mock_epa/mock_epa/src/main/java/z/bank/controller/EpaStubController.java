package z.bank.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import z.bank.config.StubProperties;
import z.bank.service.KafkaLoggingService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/epa")
public class EpaStubController {

    private final StubProperties stubProperties;
    private final KafkaLoggingService kafkaLoggingService;

    @Autowired
    public EpaStubController(StubProperties stubProperties,
                             KafkaLoggingService kafkaLoggingService) {
        this.stubProperties = stubProperties;
        this.kafkaLoggingService = kafkaLoggingService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread.sleep(stubProperties.getValidateDelay()); // Используем задержку для validate

        String response = "OK";
        long processingTime = System.currentTimeMillis() - startTime;

        kafkaLoggingService.logMockRequest(
                "POST /api/epa/validate",
                response,
                processingTime,
                stubProperties.getValidateDelay()
        );

        log.info("Processed validate request in {} ms (delay: {} ms)",
                processingTime, stubProperties.getValidateDelay());

        return ResponseEntity.ok().body(Map.of(
                "status", "OK",
                "message", "Token is valid"
        ));
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread.sleep(stubProperties.getTokenDelay()); // Используем задержку для token

        String response = "OK";
        long processingTime = System.currentTimeMillis() - startTime;

        kafkaLoggingService.logMockRequest(
                "POST /api/epa/token",
                response,
                processingTime,
                stubProperties.getTokenDelay()
        );

        log.info("Processed token request in {} ms (delay: {} ms)",
                processingTime, stubProperties.getTokenDelay());

        String stubToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJhZG1pbiI6ZmFsc2UsImV4cCI6MTYxNDMzOTcwMH0.Zh-cFKuBktWwq-w-Tr1fgLAAlULAQOo866JryPijSIQ";
        return ResponseEntity.ok().body(Map.of(
                "token", stubToken
        ));
    }

    @PostMapping("/validate/config/delay")
    public ResponseEntity<String> updateDelayValidate(@RequestParam int delay) {
        stubProperties.setValidateDelay(delay);
        kafkaLoggingService.logMockConfigChange(
                "Validate response delay changed to: " + delay + " ms"
        );
        return ResponseEntity.ok("Validate delay set to " + delay + " ms");
    }

    @PostMapping("/token/config/delay")
    public ResponseEntity<String> updateDelayToken(@RequestParam int delay) {
        stubProperties.setTokenDelay(delay);
        kafkaLoggingService.logMockConfigChange(
                "Token response delay changed to: " + delay + " ms"
        );
        return ResponseEntity.ok("Token delay set to " + delay + " ms");
    }
}