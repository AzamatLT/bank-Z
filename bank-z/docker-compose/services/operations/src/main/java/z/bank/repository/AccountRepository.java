package z.bank.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import z.bank.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.cardNumber = :cardNumber")
    void increaseBalance(@Param("cardNumber") String cardNumber,
                         @Param("amount") BigDecimal amount);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.cardNumber = :cardNumber AND a.balance >= :amount")
    int decreaseBalance(@Param("cardNumber") String cardNumber,
                        @Param("amount") BigDecimal amount);
}