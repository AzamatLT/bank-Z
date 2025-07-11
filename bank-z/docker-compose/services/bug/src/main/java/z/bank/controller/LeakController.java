package z.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class LeakController {

    // 1. Утечка через статическое поле (Classloader leak)
    private static final List<Object> STATIC_LEAK = new ArrayList<>();

    // 2. Утечка через кэш в HashMap без очистки
    private final Map<String, Object> cacheLeak = new HashMap<>();

    // 3. Пул потоков без ограничения (thread leak)
    private final ExecutorService unboundedThreadPool = Executors.newCachedThreadPool();

    // 4. Deadlock пример
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    /**
     * Утечка через статическую коллекцию.
     * Объекты добавляются в статический список и никогда не удаляются.
     */
    @GetMapping("/static-leak")
    public String staticLeak() {
        // Создаем большие объекты и добавляем в статический список
        byte[] bigObject = new byte[1024 * 1024]; // 1MB
        STATIC_LEAK.add(bigObject);
        return "Added 1MB to static collection. Total size: " + STATIC_LEAK.size() + " MB";
    }

    /**
     * Утечка через кэш в HashMap.
     * Кэш растет без ограничений и никогда не очищается.
     */
    @GetMapping("/cache-leak")
    public String cacheLeak() {
        String key = "key-" + System.currentTimeMillis();
        byte[] value = new byte[512 * 1024]; // 0.5MB
        cacheLeak.put(key, value);
        return "Added 0.5MB to cache. Cache size: " + cacheLeak.size() + " items";
    }

    /**
     * Утечка потоков - запускаем потоки, которые никогда не завершаются.
     */
    @GetMapping("/thread-leak")
    public String threadLeak() {
        unboundedThreadPool.submit(() -> {
            try {
                // Поток живет вечно и ничего не делает
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return "Started new never-ending thread";
    }

    /**
     * Deadlock - два потока блокируют ресурсы в разном порядке.
     */
    @GetMapping("/deadlock")
    public String deadlock() {
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lock2) {
                    System.out.println("Thread 1 got both locks");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (lock1) {
                    System.out.println("Thread 2 got both locks");
                }
            }
        }).start();

        return "Deadlock scenario started. Check logs and thread dump.";
    }

    /**
     * Утечка через не закрытые ресурсы (имитация).
     */
    @GetMapping("/resource-leak")
    public String resourceLeak() {
        // В реальности здесь мог бы быть не закрытый InputStream, Connection и т.д.
        List<byte[]> resources = new ArrayList<>();
        resources.add(new byte[1024 * 256]); // 0.25MB
        return "Leaked 0.25MB of resources (simulated)";
    }

    /**
     * Бесконечная рекурсия (StackOverflowError).
     */
    @GetMapping("/stack-overflow")
    public String stackOverflow() {
        recursiveMethod(0);
        return "This will never be reached";
    }

    private void recursiveMethod(int counter) {
        // Нет условия выхода - приведет к StackOverflowError
        System.out.println("Recursion depth: " + counter);
        recursiveMethod(counter + 1);
    }

    /**
     * Утечка через interned строки.
     */
    @GetMapping("/string-intern-leak")
    public String stringInternLeak() {
        // Каждый вызов добавляет новую строку в пул строк
        String newString = new String("leak-" + System.currentTimeMillis()).intern();
        return "Added new interned string: " + newString;
    }

    /**
     * Проблема с блокировкой многих потоков на одном мониторе.
     */
    @GetMapping("/thread-contention")
    public String threadContention() {
        Object sharedLock = new Object();

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                synchronized (sharedLock) {
                    try {
                        // Долгая операция под блокировкой
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        return "Started 100 threads contending for the same lock";
    }
}

