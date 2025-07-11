package z.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NotificationMessage {
    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("operation_type")
    private String operationType;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("balance")
    private BigDecimal balance;
}