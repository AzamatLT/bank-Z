package z.bank.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_history")
public class OperationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "atm_number")
    private String atmNumber;

    @Column(name = "office_number")
    private String officeNumber;

    @Column(name = "currency_code")
    private Integer currencyCode;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    @Column(name = "status")
    private String status;

    @Column(name = "id_operation")
    private Integer operationId;
}
