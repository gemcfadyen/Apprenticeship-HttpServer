package server.router;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.router.HttpMethods.GET;

public class RouteLogTest {

    @Test
    public void logsRequest() {
        ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
        RouteLog routeLog = new RouteLog(resourceHandlerSpy);

        HttpRequest request = anHttpRequestBuilder()
                .withRequestLine(GET.name())
                .withRequestUri("/aFile")
                .build();

        routeLog.audit(request);

        assertThat(resourceHandlerSpy.hasAppendedToResource(), is(true));
        assertThat(resourceHandlerSpy.getNameOfResourceThatWasAppended(), is("/logs"));
        assertThat(resourceHandlerSpy.getContentAppendedToResource(), is("GET /aFile HTTP/1.1\n"));
    }
}
