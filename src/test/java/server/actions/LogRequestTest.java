package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;

public class LogRequestTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();

    @Test
    public void isEligibleForRequestUri() {
        HttpRequest httpRequest = HttpRequestBuilder.anHttpRequestBuilder()
                .withRequestUri("/requests")
                .withRequestLine(GET.name())
                .build();
        LogRequest logRequest = new LogRequest(resourceHandlerSpy);

        assertThat(logRequest.isEligible(httpRequest), is(true));
    }

    @Test
    public void isNotEligibleForOtherUris() {
        HttpRequest httpRequest = HttpRequestBuilder.anHttpRequestBuilder()
                .withRequestUri("/another")
                .withRequestLine(GET.name())
                .build();
        LogRequest logRequest = new LogRequest(resourceHandlerSpy);

        assertThat(logRequest.isEligible(httpRequest), is(false));
    }

    @Test
    public void appendsRequestToLogs() {
        HttpRequest httpRequest = HttpRequestBuilder.anHttpRequestBuilder()
                .withRequestUri("/requests")
                .withRequestLine(GET.name())
                .build();
        LogRequest logRequest = new LogRequest(resourceHandlerSpy);

        logRequest.process(httpRequest);

        assertThat(resourceHandlerSpy.hasAppendedToResource(), is(true));
        assertThat(resourceHandlerSpy.getNameOfResourceThatWasChanged(), is("/logs"));
        assertThat(resourceHandlerSpy.getContentAppendedToResource(), is("GET /requests HTTP/1.1\n"));
    }
}