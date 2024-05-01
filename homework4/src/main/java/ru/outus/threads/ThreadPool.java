package ru.outus.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.threads.internal.ThreadSafeStorage;
import ru.outus.threads.internal.ThreadPoolWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool {

    private final List<ThreadPoolWorker> workerThreads = new ArrayList<>();
    private final CountDownLatch countDownLatch;
    private final ThreadSafeStorage<Runnable> tasksStorage = new ThreadSafeStorage<>();
    private final AtomicBoolean isComplete = new AtomicBoolean();

    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);

    public ThreadPool(int threadsCount) {
        countDownLatch = new CountDownLatch(threadsCount);
        isComplete.set(false);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadsCount);
        for (int i = 0; i < threadsCount; ++i) {
            workerThreads.add(new ThreadPoolWorker("Worker-" + i,
                    tasksStorage, cyclicBarrier, countDownLatch, isComplete));
        }

        logger.info("Created " + threadsCount + " workers");
        workerThreads.forEach(Thread::start);
        logger.info("Started " + threadsCount + " workers.");

    }

    public void execute(Runnable task) {
        logger.info("Try add new task to execute ...");

        if (isComplete.get()) {
            logger.error("Add new task error. Workers completes!");
            throw new IllegalStateException();
        }

        tasksStorage.add(task);
        logger.info("Add new task success!");
    }

    public void shutdown() {
        logger.info("Shutdown ...");

        isComplete.set(true);
        logger.info("Shutdown: set isComplete flag value: " + isComplete.get());

        logger.info("Shutdown: try all workers interrupt ...");
        workerThreads.forEach(Thread::interrupt);
        logger.info("Shutdown: all workers interrupted!");

        try {
            logger.info("Shutdown: wait all thread to complete ...");
            countDownLatch.await();
            logger.info("Shutdown: thread pool complete!");
        } catch (InterruptedException ignored) {
            logger.error("Interrupted workers complete wait!");
        }
    }
}
