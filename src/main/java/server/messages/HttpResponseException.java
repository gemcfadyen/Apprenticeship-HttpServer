package server.messages;

public class HttpResponseException extends RuntimeException {
    public HttpResponseException(Throwable cause) {
        super("Error when creating Http Response", cause);
    }
}
