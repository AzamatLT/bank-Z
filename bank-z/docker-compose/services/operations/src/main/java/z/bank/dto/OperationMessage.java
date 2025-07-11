package z.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationMessage {
    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("operation_type")
    private String operationType; // "INCREASE" or "DECREASE"

    @JsonProperty("atm_number")
    private String atmNumber;

    @JsonProperty("office_number")
    private String officeNumber;

    @JsonProperty("currency_code")
    private Integer currencyCode;

    @JsonProperty("operation_id")
    private Integer operationId; // ID from dictionary_operations
}