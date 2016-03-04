package server;


import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.OK;

public class ActionStub implements Action {
    private final boolean isEligibleFlag;

    public ActionStub(boolean isEligibleFlag) {
       this.isEligibleFlag = isEligibleFlag;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return isEligibleFlag;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        return anHttpResponseBuilder()
                .withStatusCode(OK)
                .build();
    }
}
