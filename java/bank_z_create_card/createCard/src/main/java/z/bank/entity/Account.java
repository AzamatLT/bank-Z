package z.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(name = "accounts_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String inn;

    @Column(name = "id_branchs", nullable = false)
    private Integer branchId;

    @Column(name = "id_currency", nullable = false)
    private Integer currencyId;
}
