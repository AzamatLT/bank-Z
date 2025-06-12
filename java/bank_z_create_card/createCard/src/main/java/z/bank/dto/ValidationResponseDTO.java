package z.bank.dto;

import lombok.Data;

@Data
public class ValidationResponseDTO {
    private String status;
    private String message;
}