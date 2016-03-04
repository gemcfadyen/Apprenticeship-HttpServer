package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpMessageHeaderProperties.PARTIAL_CONTENT_RANGE;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.PARTIAL_CONTENT;
import static server.router.HttpMethods.GET;

public class PartialContentTest {

    private final ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private final PartialContent partialContentAction = new PartialContent(resourceHandlerSpy, new HeaderParameterExtractor());

    @Test
    public void isEligibleWhenRequestHasRangeProperties() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=0-4");

        PartialContent partialContentAction = new PartialContent(resourceHandlerSpy, new HeaderParameterExtractor());
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withHeaderParameters(headerParams)
                .withRequestLine(GET.name())
                .build();

        assertThat(partialContentAction.isEligible(httpRequest), is(true));
    }

    @Test
    public void isNotEligibleWhenRequestHasNoRangeProperties() {
        Map<String, String> headerParams = new HashMap<>();

        PartialContent partialContentAction = new PartialContent(resourceHandlerSpy, new HeaderParameterExtractor());
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withHeaderParameters(headerParams)
                .withRequestLine(GET.name())
                .build();

        assertThat(partialContentAction.isEligible(httpRequest), is(false));
    }

    @Test
    public void partialContentRequestContainsStatus206() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=0-4");

        PartialContent partialContentAction = new PartialContent(resourceHandlerSpy, new HeaderParameterExtractor());
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withHeaderParameters(headerParams)
                .withRequestLine(GET.name())
                .build();

        HttpResponse httpResponse = partialContentAction.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(PARTIAL_CONTENT));
    }

    @Test
    public void responseContainsTheContentRange() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=0-4");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(GET.name())
                .withHeaderParameters(headerParams)
                .build();

        HttpResponse httpResponse = partialContentAction.process(httpRequest);

        assertThat(httpResponse.contentRange(), is("bytes=0-4"));
        assertThat(httpResponse.body(), is("My=Da".getBytes()));
        assertThat(resourceHandlerSpy.hasReadResource(), is(true));
    }

    @Test
    public void requestWithNoEndRangeReadsUntilEndOfResource() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=4-");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(GET.name())
                .withHeaderParameters(headerParams)
                .build();

        HttpResponse httpResponse = partialContentAction.process(httpRequest);

        assertThat(httpResponse.contentRange(), is("bytes=4-7"));
        assertThat(httpResponse.body(), is("ata".getBytes()));
        assertThat(resourceHandlerSpy.hasReadResource(), is(true));
    }
}
