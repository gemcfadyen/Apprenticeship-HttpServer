package server.actions;

import server.Action;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.FOUND;
import static server.router.Resource.REDIRECT;

public class Redirect implements Action {

    @Override
    public boolean isEligible(HttpRequest request) {
        return REDIRECT.getPath().equals(request.getRequestUri());
    }

    @Override
    public HttpResponse process(HttpRequest request) {

        return anHttpResponseBuilder()
                .withStatusCode(FOUND)
                .withLocation("http://localhost:5000/")
                .build();
    }
}
