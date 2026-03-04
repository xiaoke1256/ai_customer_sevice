package jdk21;

import java.time.Duration;
import java.util.concurrent.Executors;

public class VirtualThread {
    public static void main(String[] args) {
        // 虚拟线程：轻量级，可以创建百万个！
        Thread.ofVirtual().start(() -> {
            // 模拟I/O操作
            try {
                Thread.sleep(Duration.ofSeconds(1));
                System.out.println("虚拟线程任务完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        try {
            Thread.sleep(Duration.ofMillis(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//
        // 虚拟线程池
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10_000; i++) {
                executor.submit(() -> processTask());
            }
        }
    }

    private static void processTask() {
        try {
            Thread.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("虚拟线程池任务完成");
    }

}
