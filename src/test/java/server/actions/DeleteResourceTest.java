package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.router.HttpMethods.DELETE;

public class DeleteResourceTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private DeleteResource deleteResource = new DeleteResource(resourceHandlerSpy);

    @Test
    public void isEligible() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/form")
                .withRequestLine(DELETE.name())
                .build();

        assertThat(deleteResource.isEligible(httpRequest), is(true));
    }

    @Test
    public void deleteRequestRemovesResource() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/form")
                .withRequestLine(DELETE.name())
                .build();

        HttpResponse httpResponse = deleteResource.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(StatusCode.OK));
        assertThat(resourceHandlerSpy.hasDeletedResource(), is(true));
    }
}
