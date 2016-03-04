package server.httpserver;

import server.ExecutorServiceFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolExecutorService implements ExecutorServiceFactory {
    private final int numberOfThreads;

    public FixedThreadPoolExecutorService(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public ExecutorService create() {
        return Executors.newFixedThreadPool(numberOfThreads);
    }
}
