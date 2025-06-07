package z.bank;

import com.example.bankclient.model.Client;
import com.example.bankclient.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ExternalValidationService externalValidationService;

    public void createClient(Client client) throws Exception {
        if (!externalValidationService.isValidInn(client.getInn())) {
            throw new RuntimeException("Отклонено службой безопасности");
        }
        clientRepository.save(client);
    }
}
