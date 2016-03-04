package server.httpserver;

import server.HttpSocket;
import server.ResponseFormatter;
import server.messages.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientSocket implements HttpSocket {

    private final Socket socket;
    private ResponseFormatter responseFormatter;

    public ClientSocket(Socket socket, ResponseFormatter responseFormatter) {
        this.socket = socket;
        this.responseFormatter = responseFormatter;
    }

    @Override
    public InputStream getRawHttpRequest() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            throw new ClientSocketException("Exception in socket whilst retrieving the request", e);
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new ClientSocketException("Exception whilst closing socket", e);
        }
    }

    @Override
    public void setHttpResponse(HttpResponse httpResponse) {
        try {
            socket.getOutputStream().write(responseFormatter.format(httpResponse));
        } catch (IOException e) {
            throw new ClientSocketException("Exception whilst writing request to socket", e);
        }
    }
}
