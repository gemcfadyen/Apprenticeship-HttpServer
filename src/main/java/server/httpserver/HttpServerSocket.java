package server.httpserver;

import server.HttpSocket;
import server.ResponseFormatter;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServerSocket {
    private final ServerSocket serverSocket;
    private ResponseFormatter responseFormatter;

    public HttpServerSocket(ServerSocket serverSocket, ResponseFormatter httpResponseFormatter) {
       this.serverSocket = serverSocket;
        responseFormatter = httpResponseFormatter;
    }

    public HttpSocket accept() {
        try {
            return new ClientSocket(serverSocket.accept(), responseFormatter);
        } catch (IOException e) {
            throw new HttpServerSocketException(e);
        }
    }
}


