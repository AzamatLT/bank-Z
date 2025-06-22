package z.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {
    private String inn;
    private String lastName;
    private String firstName;
    private String middleName;
    private String branchCode;
}
