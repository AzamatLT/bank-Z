package z.bank.dto;

import lombok.Data;

@Data
public class CardResponseDTO {
    private String status;
    private String message;
    private String cardNumber;
    private String expirationDate;
    private String accountNumber;
}
