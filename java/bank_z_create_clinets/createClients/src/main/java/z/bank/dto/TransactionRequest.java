package z.bank.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String accountFrom;
    private String accountTo;
    private double amount;
    private String currency;
    private String description;
}