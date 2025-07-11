package z.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableKafka
@EnableAsync
@EnableJpaRepositories(basePackages = "z.bank.repository")
@EntityScan(basePackages = "z.bank.model")
public class CardOperationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardOperationServiceApplication.class, args);
    }
}