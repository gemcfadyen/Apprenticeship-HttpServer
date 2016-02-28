package server;

import java.util.concurrent.ExecutorService;

public class HttpServer {
    private final HttpServerSocket serverSocket;
    private final RequestParser requestParser;
    private RouteProcessor httpRouteProcessor;
    private ExecutorServiceFactory executorServiceFactory;

    public HttpServer(HttpServerSocket serverSocket,
                      RequestParser requestParser,
                      RouteProcessor httpRouteProcessor,
                      ExecutorServiceFactory requestExecutorService) {
        this.serverSocket = serverSocket;
        this.requestParser = requestParser;
        this.httpRouteProcessor = httpRouteProcessor;
        this.executorServiceFactory = requestExecutorService;
    }

    public void processRequest() {
        System.out.println("Listening for request.....");
        ExecutorService executor = executorServiceFactory.create();
        ProcessClientRequestTask task = new ProcessClientRequestTask(serverSocket.accept(), requestParser, httpRouteProcessor);
        ThreadExecutorService requestProcessorThread = new RequestThreadExecutorService(executor);
        requestProcessorThread.execute(task);
        requestProcessorThread.shutdown();
    }
}


