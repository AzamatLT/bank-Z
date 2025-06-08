package z.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {
    private String lastName;
    private String firstName;
    private String middleName;
    private String inn;
    private String branchCode;
}
