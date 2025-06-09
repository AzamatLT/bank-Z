package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z.bank.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByInn(String inn);
}


