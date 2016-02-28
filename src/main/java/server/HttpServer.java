package server;

import server.messages.HttpRequest;

public class HttpServer {
    private final HttpServerSocket serverSocket;
    private final RequestParser requestParser;
    private RouteProcessor httpRouteProcessor;

    public HttpServer(HttpServerSocket serverSocket,
                      RequestParser requestParser,
                      RouteProcessor httpRouteProcessor) {
        this.serverSocket = serverSocket;
        this.requestParser = requestParser;
        this.httpRouteProcessor = httpRouteProcessor;
    }

    public void processRequest() {
        System.out.println("Listening for request.....");

        HttpSocket client = serverSocket.accept();
       // HttpRequest httpRequest = requestParser.parse(client.getRawHttpRequest());

        //client.setHttpResponse(httpRouteProcessor.process(httpRequest));
        //client.close();
    }
}
