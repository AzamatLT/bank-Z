package z.bank.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    @Column(name = "accounts_date")
    private LocalDateTime creationDate;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "inn")
    private String inn;

    @Column(name = "id_branchs")
    private Integer branchId;

    @Column(name = "id_currency")
    private Integer currencyId;

    @Column(name = "balance")
    private BigDecimal balance;
}
