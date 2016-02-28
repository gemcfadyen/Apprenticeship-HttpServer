package server;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServerSocket {
    private final ServerSocket serverSocket;
    private ResponseFormatter responseFormatter;

    public HttpServerSocket(ServerSocket serverSocket, ResponseFormatter httpResponseFormatter) {
       this.serverSocket = serverSocket;
       this.responseFormatter = httpResponseFormatter;
    }

    public HttpSocket accept() {
        try {
            return new ClientSocket(serverSocket.accept(), responseFormatter);
        } catch (IOException e) {
            throw new HttpServerSocketException("Exception occurred whilst server was accepting client requests", e);
        }
    }
}


