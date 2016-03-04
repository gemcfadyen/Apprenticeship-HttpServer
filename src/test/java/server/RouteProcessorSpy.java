package server;

import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.HttpResponseBuilder;
import server.messages.StatusCode;

public class RouteProcessorSpy implements RouteProcessor {
    private boolean hasProcessed = false;

    public boolean hasProcessed() {
        return hasProcessed;
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        hasProcessed = true;
        return HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .build();
    }
}
