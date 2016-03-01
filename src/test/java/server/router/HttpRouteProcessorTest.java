package server.router;

import org.junit.Before;
import org.junit.Test;
import server.Action;
import server.ResourceHandler;
import server.ResourceHandlerSpy;
import server.actions.ListResourcesInPublicDirectory;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.METHOD_NOT_ALLOWED;
import static server.messages.StatusCode.NOT_FOUND;
import static server.messages.StatusCode.OK;
import static server.router.Route.HOME;

public class HttpRouteProcessorTest {
    private HttpRouteProcessor requestProcessor;

    @Before
    public void setup() {
        requestProcessor = new HttpRouteProcessor(new RoutesStub(new ResourceHandlerSpy()));
    }

    @Test
    public void routesNotConfiguredResultIn404Response() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/unknown/route")
                .withRequestLine(GET.name())
                .build();

        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(NOT_FOUND));
    }

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
    private ResourceHandler resourceHandler;

    public RoutesStub(ResourceHandler resourceHandler) {
        super(resourceHandler);
        this.resourceHandler = resourceHandler;

    }

    public Map<RoutingCriteria, Action> routes() {
        Map<RoutingCriteria, Action> routesForTest = new HashMap<>();
        routesForTest.put(new RoutingCriteria(HOME, GET), new ListResourcesInPublicDirectory(resourceHandler));
        return routesForTest;
    }
}
