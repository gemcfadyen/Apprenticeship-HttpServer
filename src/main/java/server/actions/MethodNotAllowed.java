package server.actions;

import server.Action;
import server.HttpMethods;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowed implements Action {

    @Override
    public HttpResponse process(HttpRequest request) {

        return anHttpResponseBuilder()
                .withStatusCode(METHOD_NOT_ALLOWED)
                .withAllowMethods(HttpMethods.values())
                .build();
    }
}
