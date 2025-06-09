package z.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import z.bank.dto.CreateClientRequest;
import z.bank.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Создание клиента", description = "Операции с клиентами банка")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;


    @Operation(summary = "Создать нового клиента")
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequest request) {
        return clientService.createClient(request);
    }
}