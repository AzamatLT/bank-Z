package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z.bank.entity.ClientHistory;

public interface ClientHistoryRepository extends JpaRepository<ClientHistory, Long> {}

