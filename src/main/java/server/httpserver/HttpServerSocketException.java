package server.httpserver;

public class HttpServerSocketException extends RuntimeException {
    public HttpServerSocketException(Throwable e) {
        super("Exception occurred whilst server was accepting client requests", e);
    }
}
