package server.actions;

import server.Action;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.router.HttpMethods;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.METHOD_NOT_ALLOWED;

public class MethodNotAllowed implements Action {

    private HeaderParameterExtractor headerParameterExtractor;

    public MethodNotAllowed(HeaderParameterExtractor headerParameterExtractor) {
        this.headerParameterExtractor = headerParameterExtractor;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return headerParameterExtractor.isReadOnly(request.headerParameters());
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        return anHttpResponseBuilder()
                .withStatusCode(METHOD_NOT_ALLOWED)
                .withAllowMethods(HttpMethods.values())
                .build();
    }
}
