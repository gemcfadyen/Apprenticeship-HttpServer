package server;

import server.messages.HttpRequest;

public class ProcessRequestTask implements Runnable {
    private HttpSocket client;
    private RequestParser requestParser;
    private RouteProcessor routeProcessor;

    public ProcessRequestTask(HttpSocket client, RequestParser requestParser, RouteProcessor routeProcessor) {
        this.client = client;
        this.requestParser = requestParser;
        this.routeProcessor = routeProcessor;
    }

    @Override
    public void run() {
        HttpRequest httpRequest = requestParser.parse(client.getRawHttpRequest());
        client.setHttpResponse(routeProcessor.process(httpRequest));
        client.close();
    }
}
