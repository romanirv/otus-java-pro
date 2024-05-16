package ru.outus.threads.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class ThreadSafeStorage<T> {

    private static final Logger logger = LoggerFactory.getLogger(ThreadSafeStorage.class);
    private final LinkedList<T> internalData = new LinkedList<>();
    private final Object monitor = new Object();

    public void add(T value) {
        synchronized (monitor){
            internalData.add(value);
            logger.info("Added new value to storage. Notify!");
            monitor.notify();
        }
    }

    public T pool() throws InterruptedException {
        synchronized (monitor) {
            while (internalData.isEmpty()) {
                logger.info("Storage is empty, wait...");
                monitor.wait();
                logger.info("Wait stop. Storage size=" + internalData.size());
            }
            T res = internalData.removeFirst();
            logger.info("Get value success!");
            return res;
        }
    }
}
