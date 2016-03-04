package server;

public interface ThreadExecutorService {
    void execute(Runnable r);
    void shutdown();
}
