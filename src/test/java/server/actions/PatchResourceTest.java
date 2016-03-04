package server.actions;

import org.junit.Test;
import server.ResourceHandlerSpy;
import server.actions.etag.EtagGenerator;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static server.actions.etag.EtagGenerationAlgorithm.SHA_1;
import static server.messages.HttpMessageHeaderProperties.CONTENT_LENGTH;
import static server.messages.HttpMessageHeaderProperties.IF_MATCH;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.NO_CONTENT;
import static server.messages.StatusCode.PRECONDITION_FAILED;
import static server.router.HttpMethods.PATCH;

public class PatchResourceTest {

    private final EtagGenerator etagGenerator = new EtagGenerator(SHA_1) {
        public String calculateEtag(byte[] value) {
            return "anEtagValue";
        }
    };
    private final ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private HeaderParameterExtractor headerParameterExtractor = new HeaderParameterExtractor();
    private final PatchResource patchResource = new PatchResource(resourceHandlerSpy, etagGenerator, headerParameterExtractor);

    @Test
    public void isEligibleWhenHeaderHasEtag() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withHeaderParameters(getPatchRequestHeaderProperties())
                .withBody("patched content")
                .build();

        assertThat(patchResource.isEligible(httpRequest), is(true));
    }

    @Test
    public void isNotEligibleWhenHeaderHasNoEtag() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withBody("patched content")
                .build();

        assertThat(patchResource.isEligible(httpRequest), is(false));
    }

    @Test
    public void patchingResourceReturns204() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withHeaderParameters(getPatchRequestHeaderProperties())
                .build();

        HttpResponse httpResponse = patchResource.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(NO_CONTENT));
    }

    @Test
    public void patchResponseHaveNoBody() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withHeaderParameters(getPatchRequestHeaderProperties())
                .build();

        HttpResponse httpResponse = patchResource.process(httpRequest);

        assertThat(httpResponse.body(), nullValue());
    }

    @Test
    public void patchResourceIfEtagMatchesOriginalContent() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withHeaderParameters(getPatchRequestHeaderProperties())
                .withBody("patched content")
                .build();

        HttpResponse httpResponse = patchResource.process(httpRequest);

        assertThat(httpResponse.eTag(), is("anEtagValue"));
        assertThat(resourceHandlerSpy.hasReadResource(), is(true));
        assertThat(resourceHandlerSpy.hasPatchedResource(), is(true));
    }

    @Test
    public void dontPatchResourceIfEtagMatchesOriginalContent() {
        PatchResource patchResource = new PatchResource(resourceHandlerSpy, eTagDictionaryReturningNotFound(), headerParameterExtractor);

        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/partial_content.txt")
                .withRequestLine(PATCH.name())
                .withHeaderParameters(getPatchRequestHeaderProperties())
                .withBody("patched content")
                .build();

        HttpResponse httpResponse = patchResource.process(httpRequest);

        assertThat(httpResponse.eTag(), nullValue());
        assertThat(httpResponse.statusCode(), is(PRECONDITION_FAILED));
        assertThat(resourceHandlerSpy.hasReadResource(), is(true));
        assertThat(resourceHandlerSpy.hasPatchedResource(), is(false));
    }

    private EtagGenerator eTagDictionaryReturningNotFound() {
        return new EtagGenerator(SHA_1) {
            public String calculateEtag(byte[] value) {
                return "the wrong value";
            }
        };
    }

    private Map<String, String> getPatchRequestHeaderProperties() {
        Map<String, String> patchHeaderParams = new HashMap<>();
        patchHeaderParams.put(IF_MATCH.getPropertyName(), "anEtagValue");
        patchHeaderParams.put(CONTENT_LENGTH.getPropertyName(), "15");
        return patchHeaderParams;
    }
}
