package z.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Для асинхронных методов
public class MemoryLeakDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemoryLeakDemoApplication.class, args);
    }
}

//static-leak - утечка через статическую коллекцию
//cache-leak - утечка через неограниченный кэш
//thread-leak - утечка потоков
//deadlock - создание deadlock ситуации
//resource-leak - утечка ресурсов (имитация)
//stack-overflow - бесконечная рекурсия
//string-intern-leak - утечка через пул строк
//thread-contention - конкуренция за ресурсы