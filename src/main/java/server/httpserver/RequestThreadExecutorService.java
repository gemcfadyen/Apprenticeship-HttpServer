package server.httpserver;

import server.ThreadExecutorService;

import java.util.concurrent.ExecutorService;

public class RequestThreadExecutorService implements ThreadExecutorService {

    private final ExecutorService executor;

    public RequestThreadExecutorService(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
