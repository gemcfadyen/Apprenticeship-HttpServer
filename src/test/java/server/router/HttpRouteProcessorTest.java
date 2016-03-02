package server.router;

import org.junit.Before;
import org.junit.Test;
import server.Action;
import server.HttpMethods;
import server.ResourceHandler;
import server.ResourceHandlerSpy;
import server.actions.ActionStub;
import server.actions.ListResourcesInPublicDirectory;
import server.actions.ReadResource;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.*;
import static server.router.Route.HOME;

public class HttpRouteProcessorTest {
    private HttpRouteProcessor requestProcessor;

    @Before
    public void setup() {
        requestProcessor = new HttpRouteProcessor(
                new RoutesStub(new ResourceHandlerSpy()));
    }

//    @Test
//    public void routesNotConfiguredResultIn404Response() {
//        HttpRequest httpRequest = anHttpRequestBuilder()
//                .withRequestUri("/unknown/route")
//                .withRequestLine(GET.name())
//                .build();
//
//        HttpResponse httpResponse = requestProcessor.process(httpRequest);
//
//        assertThat(httpResponse.statusCode(), is(NOT_FOUND));
//    }

    @Test
    public void routesConfiguredUri() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri(HOME.getPath())
                .withRequestLine(GET.name())
                .build();

        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(OK));
    }

    @Test
    public void methodNotSupportedRoutesTo302() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri(HOME.getPath())
                .withRequestLine("UnknownVerb")
                .build();

        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(METHOD_NOT_ALLOWED));
    }
}

class RoutesStub extends Routes {

    public RoutesStub(ResourceHandler resourceHandler) {
        super(resourceHandler);
    }

    public Map<HttpMethods, List<Action>> routes() {
        Map<HttpMethods, List<Action>> routesForTest = new HashMap<>();
        List<Action> actions = new ArrayList<>();
        actions.add(new ActionStub());
        routesForTest.put(GET, actions);
        return routesForTest;
    }
}

