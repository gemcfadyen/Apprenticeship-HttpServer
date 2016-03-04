package server.httpserver;

import java.io.IOException;

public class ClientSocketException extends RuntimeException {
    public ClientSocketException(String message, IOException cause) {
        super(message, cause);
    }
}
