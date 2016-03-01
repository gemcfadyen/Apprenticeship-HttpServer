package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.OK;
import static server.HttpMethods.POST;

public class WriteResourceTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private WriteResource writeResource = new WriteResource(resourceHandlerSpy);

    @Test
    public void postMethodCreatesAResource() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/form")
                .withBody("content")
                .withRequestLine(POST.name())
                .build();

        HttpResponse httpResponse = writeResource.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(OK));
        assertThat(httpResponse.body(), is("content".getBytes()));
        assertThat(resourceHandlerSpy.hasWrittenToResource(), is(true));
    }
}
