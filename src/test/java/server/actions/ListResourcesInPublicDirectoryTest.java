package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;
import server.HttpMethods;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class ListResourcesInPublicDirectoryTest {

    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private ListResourcesInPublicDirectory listResources = new ListResourcesInPublicDirectory(resourceHandlerSpy);

    @Test
    public void actionIsEligibleForHomeUri() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/")
                .withRequestLine(HttpMethods.GET.name())
                .build();

        assertThat(listResources.isEligible(httpRequest), is(true));
    }

    @Test
    public void actionIsNotEligibleForAnotherUri() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/another")
                .withRequestLine(HttpMethods.GET.name())
                .build();

        assertThat(listResources.isEligible(httpRequest), is(false));
    }

    @Test
    public void responseContainsFilenamesAsLinks() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/")
                .withRequestLine(HttpMethods.GET.name())
                .build();

        HttpResponse httpResponse = listResources.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(StatusCode.OK));
        assertThat(httpResponse.body(), is("<a href=/file1>file1</a><br><a href=/file2>file2</a>".getBytes()));
        assertThat(resourceHandlerSpy.hasGotDirectoryContent(), is(true));
    }
}