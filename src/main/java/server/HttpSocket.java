package server;

import server.messages.HttpResponse;

import java.io.InputStream;

public interface HttpSocket {
    InputStream getRawHttpRequest();
    void close();
    void setHttpResponse(HttpResponse httpResponse);
}
