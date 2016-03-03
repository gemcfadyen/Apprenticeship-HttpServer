package server.actions;

public class EtagGeneratorException extends RuntimeException {
    public EtagGeneratorException(Throwable cause) {
        super("Exception thrown whilst generating etag", cause);

    }
}
