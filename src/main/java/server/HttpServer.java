package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final HttpServerSocket serverSocket;
    private final RequestParser requestParser;
    private RouteProcessor httpRouteProcessor;
    private ThreadServiceFactory threadPoolExecutor;

    public HttpServer(HttpServerSocket serverSocket,
                      RequestParser requestParser,
                      RouteProcessor httpRouteProcessor,
                      ThreadServiceFactory threadPoolExecutor) {
        this.serverSocket = serverSocket;
        this.requestParser = requestParser;
        this.httpRouteProcessor = httpRouteProcessor;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void processRequest() {
        System.out.println("Listening for request.....");
        ProcessRequestTask processRequestTask = new ProcessRequestTask(serverSocket.accept(), requestParser, httpRouteProcessor);
        ThreadExecutorService threadExecutorService = threadPoolExecutor.createPoolWithThreads();
        threadExecutorService.execute(processRequestTask);
        threadExecutorService.shutdown();
    }
}

interface ThreadServiceFactory {
    ThreadExecutorService createPoolWithThreads();
}

interface ThreadExecutorService {
    void execute(Runnable r);
    void shutdown();
}

class ThreadExecutor implements ThreadExecutorService {

    private ExecutorService executorService;

    public ThreadExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void execute(Runnable r) {
        executorService.execute(r);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}

class FixedThreadPoolExecutor implements ThreadServiceFactory {
    private int numberOfThreads;

    public FixedThreadPoolExecutor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @Override
    public ThreadExecutorService createPoolWithThreads() {
        return new ThreadExecutor(Executors.newFixedThreadPool(numberOfThreads));
    }
}
