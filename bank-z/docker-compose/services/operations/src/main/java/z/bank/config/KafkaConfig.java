package z.bank.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${app.topics.operations}")
    private String operationsTopic;

    @Value("${app.topics.notifications}")
    private String notificationsTopic;

    @Value("${app.topics.logs}")
    private String logsTopic;

    @Bean
    public NewTopic operationsTopic() {
        return TopicBuilder.name(operationsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name(notificationsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic logsTopic() {
        return TopicBuilder.name(logsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
