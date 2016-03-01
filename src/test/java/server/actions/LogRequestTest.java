package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpRequestBuilder;
import server.HttpMethods;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LogRequestTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();

    @Test
    public void appendsRequestToLogs() {
        HttpRequest httpRequest = HttpRequestBuilder.anHttpRequestBuilder()
                .withRequestUri("/log")
                .withRequestLine(HttpMethods.GET.name())
                .build();
        LogRequest logRequest = new LogRequest(resourceHandlerSpy);

        logRequest.process(httpRequest);

        assertThat(resourceHandlerSpy.hasAppendedToResource(), is(true));
        assertThat(resourceHandlerSpy.getNameOfResourceThatWasChanged(), is("/logs"));
        assertThat(resourceHandlerSpy.getContentAppendedToResource(), is("GET /log HTTP/1.1\n"));
    }
}