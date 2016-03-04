package server.httpserver;

import server.HttpSocket;
import server.RequestParser;
import server.RouteProcessor;
import server.messages.HttpRequest;

public class ProcessClientRequestTask implements Runnable {
    private final HttpSocket client;
    private final RequestParser requestParser;
    private final RouteProcessor routeProcessor;

    public ProcessClientRequestTask(HttpSocket client, RequestParser requestParser, RouteProcessor routeProcessor) {
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
