package z.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogMessage {
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("service")
    private String service = "card-operation-service";

    @JsonProperty("level")
    private String level;

    @JsonProperty("message")
    private String message;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("operation_type")
    private String operationType;

    @JsonProperty("status")
    private String status;

    @JsonProperty("details")
    private String details;
}
