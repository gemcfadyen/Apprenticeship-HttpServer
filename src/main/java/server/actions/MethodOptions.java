package server.actions;

import server.Action;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.HttpMethods;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.OK;

public class MethodOptions implements Action {

    @Override
    public boolean isEligible(HttpRequest request) {
        return true;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        return anHttpResponseBuilder()
                .withStatusCode(OK)
                .withAllowMethods(HttpMethods.values())
                .build();
    }
}
