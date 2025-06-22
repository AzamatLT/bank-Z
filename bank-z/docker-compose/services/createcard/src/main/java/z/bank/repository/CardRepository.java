package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z.bank.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {}

