package server;

public class ThreadExecutorServiceSpy implements ThreadExecutorService {
    private boolean hasExecuted;
    private boolean hasShutdown;

    @Override
    public void execute(Runnable r) {
        if(!hasShutdown) {
            hasExecuted = true;
        } else {
            throw new RuntimeException("Execute is being called when the executor is shutdwn");
        }

    }

    @Override
    public void shutdown() {
        hasShutdown = true;
    }

    public boolean hasExecutedRunnable() {
        return hasExecuted;
    }

    public boolean hasShutdown() {
        return hasShutdown;
    }
}
