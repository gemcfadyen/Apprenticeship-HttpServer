package server.httpserver;

import server.HttpSocket;
import server.messages.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ClientSpy implements HttpSocket {
    private boolean isClosed = false;
    private boolean hasHttpResponse = false;

    @Override
    public InputStream getRawHttpRequest() {
        return new ByteArrayInputStream("An Http Request".getBytes());
    }

    @Override
    public void close() {
        isClosed = true;
    }

    @Override
    public void setHttpResponse(HttpResponse httpResponse) {
        hasHttpResponse = true;
    }


    public boolean isClosed() {
       return isClosed;
    }

    public boolean hasHttpResponse() {
       return hasHttpResponse;
    }
}
