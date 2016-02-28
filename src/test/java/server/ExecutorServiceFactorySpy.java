package server;

public class ExecutorServiceFactorySpy implements ThreadServiceFactory {
    private boolean hasInitialisedPool;
    private ThreadExecutorServiceSpy threadExecutorServiceSpy;

    ExecutorServiceFactorySpy(ThreadExecutorServiceSpy threadExecutorServiceSpy) {
        this.threadExecutorServiceSpy = threadExecutorServiceSpy;
    }

    @Override
    public ThreadExecutorService createPoolWithThreads() {
        hasInitialisedPool = true;
        return threadExecutorServiceSpy;
    }

    public boolean hasInitialisedThreadPool() {
        return hasInitialisedPool;
    }
}
