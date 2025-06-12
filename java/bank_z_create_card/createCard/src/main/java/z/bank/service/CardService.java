package z.bank.service;

import z.bank.dto.CardRequestDTO;
import z.bank.dto.CardResponseDTO;
import z.bank.dto.ValidationResponseDTO;
import z.bank.entity.*;
import z.bank.repository.*;
import z.bank.util.AccountNumberGenerator;
import z.bank.util.CardNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import z.bank.config.KafkaLoggingService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CardService {
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final ClientHistoryRepository historyRepository;
    private final SettingRepository settingRepository;
    private final KafkaLoggingService kafkaLoggingService;
    private final RestTemplate restTemplate;

    @Value("${validation.service.url}")
    private String validationServiceUrl;

    @Autowired
    public CardService(ClientRepository clientRepository, CardRepository cardRepository,
                       AccountRepository accountRepository, ClientHistoryRepository historyRepository,
                       SettingRepository settingRepository, KafkaLoggingService kafkaLoggingService,
                       RestTemplate restTemplate) {
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.historyRepository = historyRepository;
        this.settingRepository = settingRepository;
        this.kafkaLoggingService = kafkaLoggingService;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public ResponseEntity<CardResponseDTO> createCard(CardRequestDTO request) {
        // Логирование запроса
        Map<String, Object> logData = new HashMap<>();
        logData.put("operation", "create_card");
        logData.put("inn", request.getInn());
        logData.put("branchId", request.getBranchId());

        // Проверка существования клиента
        Optional<Client> clientOpt = clientRepository.findByInn(request.getInn());
        if (clientOpt.isEmpty()) {
            CardResponseDTO response = new CardResponseDTO();
            response.setStatus("ERROR");
            response.setMessage("Клиент не зарегистрирован");

            logData.put("status", "client_not_found");
            kafkaLoggingService.logOperation("card_creation", logData);
            System.out.println("Would send to Kafka: {}" + logData);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Валидация клиента во внешнем сервисе
        try {
            ResponseEntity<ValidationResponseDTO> validationResponse = restTemplate.getForEntity(
                    validationServiceUrl + "?inn=" + request.getInn(),
                    ValidationResponseDTO.class);

            if (!validationResponse.getStatusCode().is2xxSuccessful()) {
                CardResponseDTO response = new CardResponseDTO();
                response.setStatus("ERROR");
                response.setMessage("Сервис временно не доступен");

                logData.put("status", "validation_service_error");
                kafkaLoggingService.logOperation("card_creation", logData);

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }

            ValidationResponseDTO validationBody = validationResponse.getBody();
            if (validationBody != null && "rejection".equals(validationBody.getStatus())) {
                CardResponseDTO response = new CardResponseDTO();
                response.setStatus("ERROR");
                response.setMessage("В разрешении отказано");

                logData.put("status", "validation_rejected");
                kafkaLoggingService.logOperation("card_creation", logData);

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            CardResponseDTO response = new CardResponseDTO();
            response.setStatus("ERROR");
            response.setMessage("Сервис временно не доступен");

            logData.put("status", "validation_service_exception");
            logData.put("error", e.getMessage());
            kafkaLoggingService.logOperation("card_creation", logData);

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        // Получение срока действия карты из настроек
        Setting cardExpirySetting = settingRepository.findById(1L).orElseThrow(
                () -> new RuntimeException("Setting not found for card expiry days"));
        int expiryDays = Integer.parseInt(cardExpirySetting.getValue());

        // Генерация данных карты и счета
        String cardNumber = CardNumberGenerator.generate();
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(expiryDays);
        // Генерация номера счёта (20 цифр)
        String accountNumber = AccountNumberGenerator.generate(
                String.valueOf(request.getBranchId()),
                String.valueOf(request.getCurrencyId()),
                request.getInn()
        );

        System.out.println("accountNumber________________________________" + accountNumber.toString());

        // Сохранение карты
        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setExpirationDate(expirationDate);
        card.setStatus(true);
        cardRepository.save(card);

        // Сохранение счета
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCreationDate(LocalDateTime.now());
        account.setCardNumber(cardNumber);
        account.setInn(request.getInn());
        account.setBranchId(request.getBranchId());
        account.setCurrencyId(request.getCurrencyId());
        accountRepository.save(account);

        // Запись в историю
        Client client = clientOpt.get();
        ClientHistory history = new ClientHistory();
        history.setLastName(client.getLastName());
        history.setFirstName(client.getFirstName());
        history.setMiddleName(client.getMiddleName());
        history.setBranchCode("BR" + request.getBranchId());
        history.setInn(client.getInn());
        history.setOperationDate(LocalDateTime.now());
        history.setOperationId(1); // 1 = создание карты
        historyRepository.save(history);

        // Формирование ответа
        CardResponseDTO response = new CardResponseDTO();
        response.setStatus("SUCCESS");
        response.setMessage("Карта успешно создана");
        response.setCardNumber(cardNumber);
        response.setExpirationDate(expirationDate.toString());
        response.setAccountNumber(accountNumber);

        logData.put("status", "success");
        logData.put("cardNumber", cardNumber);
        logData.put("accountNumber", accountNumber);
        kafkaLoggingService.logOperation("card_creation", logData);

        return ResponseEntity.ok(response);
    }
}
