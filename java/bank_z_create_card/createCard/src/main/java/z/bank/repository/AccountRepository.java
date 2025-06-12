package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z.bank.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}

