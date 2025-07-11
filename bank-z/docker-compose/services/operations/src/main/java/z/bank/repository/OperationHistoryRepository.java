package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import z.bank.model.OperationHistory;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long> {
}
