package server.messages;

public class HttpRequestParsingException extends RuntimeException {

    public HttpRequestParsingException(Throwable cause) {
        super("Error when parsing Http Request", cause);
    }
}
