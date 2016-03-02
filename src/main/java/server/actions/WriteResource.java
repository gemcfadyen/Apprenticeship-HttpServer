package server.actions;

import server.Action;
import server.HttpMethods;
import server.ResourceHandler;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;

public class WriteResource implements Action {
    private final ResourceHandler resourceHandler;
    private final StatusCode responseStatus;

    public WriteResource(ResourceHandler resourceHandler, StatusCode responseStatus) {
        this.resourceHandler = resourceHandler;
        this.responseStatus = responseStatus;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return true;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        resourceHandler.write(request.getRequestUri(), request.getBody());

        return anHttpResponseBuilder()
                .withStatusCode(responseStatus)
                .withAllowMethods(HttpMethods.values())
                .withBody(request.getBody().getBytes())
                .build();
    }
}
