package server;

import server.messages.HttpRequest;

public class HttpServer {
    private final String host;
    private final int port;
    private final HttpServerSocket serverSocket;
    private final RequestParser requestParser;
    private RouteProcessor httpRouteProcessor;

    public HttpServer(String host, int port,
                      HttpServerSocket serverSocket,
                      RequestParser requestParser,
                      RouteProcessor httpRouteProcessor) {
        this.host = host;
        this.port = port;
        this.serverSocket = serverSocket;
        this.requestParser = requestParser;
        this.httpRouteProcessor = httpRouteProcessor;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void processRequest() {
        System.out.println("Listening for request.....");

        new ProcessClientRequestThread(
                serverSocket.accept(),
                requestParser,
                httpRouteProcessor).start();
//        HttpRequest httpRequest = requestParser.parse(client.getRawHttpRequest());
//
//        client.setHttpResponse(httpRouteProcessor.process(httpRequest));
//        client.close();
    }
}

