package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;
import static server.StatusCode.OK;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class ReadResourceTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private ReadResource readResource = new ReadResource(resourceHandlerSpy);

    @Test
    public void getMethodLooksUpResourceForResponseBody() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/form")
                .withRequestLine(GET.name())
                .build();

        HttpResponse httpResponse = readResource.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(OK));
        assertThat(httpResponse.body(), is("My=Data".getBytes()));
        assertThat(resourceHandlerSpy.hasReadResource(), is(true));
    }

}