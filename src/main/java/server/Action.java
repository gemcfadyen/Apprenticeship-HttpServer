package server;

import server.messages.HttpRequest;
import server.messages.HttpResponse;

public interface Action {
    boolean isEligible(HttpRequest request);
    HttpResponse process(HttpRequest request);
}
