package server.httpserver;

import server.ExecutorServiceFactory;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceFactorySpy implements ExecutorServiceFactory {
    private ExecutorServiceSpy executorServiceSpy;
    private boolean hasInitialisedPool;

    public ExecutorServiceFactorySpy(ExecutorServiceSpy executorServiceSpy) {
        this.executorServiceSpy = executorServiceSpy;
    }

    @Override
    public ExecutorService create() {
        hasInitialisedPool = true;
        return executorServiceSpy;
    }

    public boolean hasInitialisedThreadPool() {
        return hasInitialisedPool;
    }
}


class ExecutorServiceSpy extends AbstractExecutorService {
    private boolean hasShutdown;
    private boolean hasExecuted;

    @Override
    public void shutdown() {
        hasShutdown = true;
    }

    @Override
    public void execute(Runnable command) {
       if(!hasShutdown) {
           hasExecuted = true;
       } else {
           throw new RuntimeException("The Executor Service has been shutdown already!");
       }
    }

    public boolean hasShutdodwn() {
        return hasShutdown;
    }

    public boolean hasExecuted() {
        return hasExecuted;
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }
}
