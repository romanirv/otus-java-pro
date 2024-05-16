package ru.outus.threads.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPoolWorker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolWorker.class);
    private final ThreadSafeStorage<Runnable> taskStorage;
    private final CyclicBarrier cyclicBarrier;
    private final CountDownLatch countDownLatch;
    private final AtomicBoolean isComplete;

    public ThreadPoolWorker(String name,
                            ThreadSafeStorage<Runnable> sharedTasksList,
                            CyclicBarrier cyclicBarrier,
                            CountDownLatch countDownLatch,
                            AtomicBoolean isComplete) {
        this.setName(name);

        this.taskStorage = sharedTasksList;
        this.cyclicBarrier = cyclicBarrier;
        this.countDownLatch = countDownLatch;
        this.isComplete = isComplete;

        logger.info("Created new worker");
    }

    @Override
    public void run() {
        logger.info("Wait, when workers started ...");
        if (!this.waitStartAllThreads())
            return;

        logger.info("All workers started!");

        while (!isComplete.get()) {
            try {
                logger.info("Try get task from storage ...");
                Runnable task = taskStorage.pool();
                logger.info("Get task from storage success");
                task.run();
                logger.info("Execute task success");
            } catch (InterruptedException e) {
                logger.info("Worker interrupt");
                break;
            }
        }
        countDownLatch.countDown();
        logger.info("Worker complete!");
    }

    private boolean waitStartAllThreads() {
        try {
            cyclicBarrier.await();
            return true;
        } catch (InterruptedException | BrokenBarrierException e) {
            logger.error("Wait start all workers exception: " + e.getLocalizedMessage());
            Thread.currentThread().interrupt();
        }
        return false;
    }
}
