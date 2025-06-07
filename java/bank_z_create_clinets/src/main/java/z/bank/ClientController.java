package z.bank;

import com.example.bankclient.model.Client;
import com.example.bankclient.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
@Tag(name = "Клиенты", description = "Операции с клиентами банка")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(description = "Создание нового клиента",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Успешно создано"),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = "text/plain"))
            })
    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody Client client) {
        try {
            clientService.createClient(client);
            return ResponseEntity.status(201).body("Клиент успешно зарегистрирован.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
