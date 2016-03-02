package server;

import server.messages.HttpRequest;
import server.messages.HttpResponse;

public interface Method {
    HttpResponse execute(HttpRequest request);
}
