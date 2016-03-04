package server.httpserver;

import server.HttpSocket;
import server.messages.HttpResponseFormatter;

public class ServerSocketSpy extends HttpServerSocket {
    private boolean isAcceptingRequests = false;
    private ClientSpy clientToReturn;

    public ServerSocketSpy(ClientSpy clientSpy) {
        super(null, new HttpResponseFormatter());
        this.clientToReturn = clientSpy;
    }

    @Override
    public HttpSocket accept() {
        isAcceptingRequests = true;
        return clientToReturn;
    }

    public boolean isAcceptingRequests() {
        return isAcceptingRequests;
    }
}
