package ru.outus.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(4);

        for (int i = 0; i < 100; ++i) {
            int taskIndex = i;
            threadPool.execute(() -> {
                logger.info("Execute task:" + taskIndex);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        try {
            logger.info("Try shutdown");
            threadPool.shutdown();

            threadPool.execute(() -> logger.info("After shutdown task"));
            logger.info("exit");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
