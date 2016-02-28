package server;

import server.messages.HttpRequest;

public class ProcessClientRequestThread extends Thread {

    private final HttpSocket client;
    private final RequestParser requestParser;
    private final RouteProcessor httpRouteProcessor;

    public ProcessClientRequestThread(HttpSocket client, RequestParser requestParser, RouteProcessor httpRouteProcessor) {
        super("ProcessClientRequestThread");
        this.client = client;
        this.requestParser = requestParser;
        this.httpRouteProcessor = httpRouteProcessor;
    }

    public void run() {
        HttpRequest httpRequest = requestParser.parse(client.getRawHttpRequest());

        client.setHttpResponse(httpRouteProcessor.process(httpRequest));
        client.close();
    }
}
