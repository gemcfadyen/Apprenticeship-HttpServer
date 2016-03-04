package server;

import server.messages.HttpRequest;
import server.messages.HttpResponse;

public interface RouteProcessor {
    HttpResponse process(HttpRequest httpRequest);
}
