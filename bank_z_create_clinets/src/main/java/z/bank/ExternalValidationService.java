package z.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalValidationService {

    @Value("${external.validation.url}")
    private String validationUrl;

    public boolean isValidInn(String inn) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                validationUrl + "/" + inn,
                Boolean.class
        );
    }
}
