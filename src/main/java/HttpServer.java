public class HttpServer {
    private final String host;
    private final int port;
    private final HttpServerSocket serverSocket;
    private final HttpRequestParser httpRequestParser;
    private final HttpResponseBuilder httpResponseBuilder;

    public HttpServer(String host, int port, HttpServerSocket serverSocket, HttpRequestParser httpRequestParser, HttpResponseBuilder httpResponseBuilder) {
        this.host = host;
        this.port = port;
        this.serverSocket = serverSocket;
        this.httpRequestParser = httpRequestParser;
        this.httpResponseBuilder = httpResponseBuilder;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void start() {
        HttpSocket client = serverSocket.accept();
        httpRequestParser.parse(client.getRawHttpRequest());

        //Route and process request

        client.setHttpResponse(httpResponseBuilder.build());
        client.close();
    }
}

interface HttpResponseBuilder {
    HttpResponse build();
}

class HttpResponse {

}

class HttpResponseBuilderSpy implements HttpResponseBuilder {
    private boolean hasBuiltHttpResponse;

    @Override
    public HttpResponse build() {
        hasBuiltHttpResponse = true;
        return new HttpResponse();
    }

    public boolean hasBuiltHttpResponse() {
        return hasBuiltHttpResponse;
    }
}
