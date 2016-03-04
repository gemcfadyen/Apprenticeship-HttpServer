package server.actions;

import server.Action;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.NOT_FOUND;
import static server.router.Resource.FOOBAR;

public class UnknownRoute implements Action {
    @Override
    public boolean isEligible(HttpRequest request) {
        return FOOBAR.getPath().equals(request.getRequestUri());
    }

    @Override
    public HttpResponse process(HttpRequest request) {

        return anHttpResponseBuilder()
                .withStatusCode(NOT_FOUND)
                .build();
    }
}
