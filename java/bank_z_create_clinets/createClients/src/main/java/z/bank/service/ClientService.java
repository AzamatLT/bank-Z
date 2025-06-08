package z.bank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import z.bank.dto.CreateClientRequest;
import z.bank.model.Client;
import z.bank.model.ClientHistory;
import z.bank.repository.ClientHistoryRepository;
import z.bank.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientHistoryRepository historyRepository;
    private final LoggingService loggingService;
    private final RestTemplate restTemplate;

    public ResponseEntity<?> createClient(CreateClientRequest request) {
        // 1. Проверка токена в ЕПА
        validateWithEpa();

        // 2. Проверка существования клиента
        Optional<Client> existingClient = clientRepository.findByInn(request.getInn());

        if (existingClient.isPresent()) {
            loggingService.logToKafka("INFO",
                    "Client already exists: " + request.getInn());
            return ResponseEntity.ok(Map.of(
                    "status", "exists",
                    "message", "Client with INN " + request.getInn() + " already exists"
            ));
        }

        // 3. Создание клиента
        Client newClient = new Client();
        newClient.setInn(request.getInn());
        newClient.setLastName(request.getLastName());
        newClient.setFirstName(request.getFirstName());
        newClient.setMiddleName(request.getMiddleName());
        clientRepository.save(newClient);

        // 4. Запись в историю
        ClientHistory history = new ClientHistory();
        history.setLastName(request.getLastName());
        history.setFirstName(request.getFirstName());
        history.setMiddleName(request.getMiddleName());
        history.setBranchCode(request.getBranchCode());
        history.setOperationDate(LocalDateTime.now());
        historyRepository.save(history);

        loggingService.logToKafka("INFO",
                "Client created: " + request.getInn());

        return ResponseEntity.ok(Map.of(
                "status", "created",
                "message", "Client created successfully"
        ));
    }

    private void validateWithEpa() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJhZG1pbiI6ZmFsc2UsImV4cCI6MTYxNDMzOTcwMH0.Zh-cFKuBktWwq-w-Tr1fgLAAlULAQOo866JryPijSIQ");

        try {
            restTemplate.exchange(
                    "http://epa-service/api/epa/validate",
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    Map.class
            );
        } catch (Exception e) {
            loggingService.logToKafka("ERROR",
                    "EPA validation failed: " + e.getMessage());
            throw new RuntimeException("EPA validation failed");
        }
    }
}
