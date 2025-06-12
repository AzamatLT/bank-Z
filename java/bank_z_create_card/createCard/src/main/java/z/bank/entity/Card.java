package z.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(name = "date_burn", nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean status = true;
}