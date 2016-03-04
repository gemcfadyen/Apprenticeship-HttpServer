package server.actions;

import server.Action;
import server.ResourceHandler;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;

public class DeleteResource implements Action {
    private final ResourceHandler resourceHandler;

    public DeleteResource(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return true;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        resourceHandler.delete(request.getRequestUri());

        return anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .build();
    }
}
