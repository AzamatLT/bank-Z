package z.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardRequestDTO {
    @NotBlank(message = "INN is required")
    private String inn;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotNull(message = "Branch ID is required")
    private Integer branchId;

    @NotNull(message = "Currency ID is required")
    private Integer currencyId;
}




