package z.bank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stub.mock")
public class StubProperties {
    private int validateDelay = 100; // задержка для validate
    private int tokenDelay = 100;    // задержка для token

    public int getValidateDelay() {
        return validateDelay;
    }

    public void setValidateDelay(int validateDelay) {
        this.validateDelay = validateDelay;
    }

    public int getTokenDelay() {
        return tokenDelay;
    }

    public void setTokenDelay(int tokenDelay) {
        this.tokenDelay = tokenDelay;
    }
}