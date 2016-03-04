package server.messages;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpMessageHeaderProperties.AUTHORISATION;
import static server.messages.HttpMessageHeaderProperties.PARTIAL_CONTENT_RANGE;

public class HeaderParameterExtractorTest {
    private HeaderParameterExtractor headerParameterExtractor = new HeaderParameterExtractor();

    @Test
    public void hasAuthenticationCredentials() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(AUTHORISATION.getPropertyName(), "Basic someEncodedValue");

        boolean hasAuthenticationCredentials = headerParameterExtractor.hasAuthenticationCredentials(headerParams);

        assertThat(hasAuthenticationCredentials, is(true));
    }

    @Test
    public void hasNotGotAuthenticationCredentials() {
        Map<String, String> headerParams = new HashMap<>();

        boolean hasAuthenticationCredentials = headerParameterExtractor.hasAuthenticationCredentials(headerParams);

        assertThat(hasAuthenticationCredentials, is(false));
    }

    @Test
    public void extractsAuthenticationCredentials() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(AUTHORISATION.getPropertyName(), "Basic someEncodedValue");

        String authenticationCredentials = headerParameterExtractor.getAuthenticationCredentials(headerParams);

        assertThat(authenticationCredentials, is("Basic someEncodedValue"));
    }

    @Test
    public void hasPartialContentRange() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=0-4");

        assertThat(headerParameterExtractor.hasPartialContentRange(headerParams), is(true));
    }

    @Test
    public void hasNotGotPartialContentRange() {
        Map<String, String> headerParams = new HashMap<>();

        assertThat(headerParameterExtractor.hasPartialContentRange(headerParams), is(false));
    }

    @Test
    public void upperAndLowerBoundariesProvidedForPartialContent() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=0-4");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(0));
        assertThat(partialContentRange.getFinishingIndex(), is(4));
    }

    @Test
    public void defaultsToZeroIfPartialContentRangeIsNotNumeric() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=A-Z");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(0));
        assertThat(partialContentRange.getFinishingIndex(), is(0));
    }

    @Test
    public void partialContentWithNoLowerBoundaryIsCorrectlyDetermined() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=-4");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(12));
        assertThat(partialContentRange.getFinishingIndex(), is("resource-content".length()));
    }

    @Test
    public void defaultsToMatchingBoundariesIfUpperBoundaryIsNonNumeric() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=-Z");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(16));
        assertThat(partialContentRange.getFinishingIndex(), is(16));
    }

    @Test
    public void partialContentWithNoUpperBoundaryIsSetToLengthOfResource() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=4-");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(4));
        assertThat(partialContentRange.getFinishingIndex(), is("resource-content".length()));
    }

    @Test
    public void defaultsToReadingAllContentIfLowerBoundaryNonNumeric() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(PARTIAL_CONTENT_RANGE.getPropertyName(), "bytes=Z-");

        Range partialContentRange = headerParameterExtractor.getPartialContentRange(headerParams, "resource-content".length());

        assertThat(partialContentRange.getStartingIndex(), is(0));
        assertThat(partialContentRange.getFinishingIndex(), is(16));
    }

    @Test
    public void isReadOnly() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(HttpMessageHeaderProperties.CONTENT_LENGTH.getPropertyName(), "0");

        assertThat(headerParameterExtractor.isReadOnly(headerParams), is(true));
    }

    @Test
    public void isNotReadOnly() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(HttpMessageHeaderProperties.CONTENT_LENGTH.getPropertyName(), "11");

        assertThat(headerParameterExtractor.isReadOnly(headerParams), is(false));
    }

    @Test
    public void ifNoContentLengthProvidedResourceIsReadOnly() {
        Map<String, String> headerParams = new HashMap<>();

        assertThat(headerParameterExtractor.isReadOnly(headerParams), is(true));
    }

    @Test
    public void ifNonNumericValueForContentLengthProvidedResourceIsReadOnly() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(HttpMessageHeaderProperties.CONTENT_LENGTH.getPropertyName(), "Cat");

        assertThat(headerParameterExtractor.isReadOnly(headerParams), is(true));
    }

    @Test
    public void hasEtagProperty() {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(HttpMessageHeaderProperties.IF_MATCH.getPropertyName(), "qwerty");

        assertThat(headerParameterExtractor.hasEtagProperty(headerParams), is(true));
    }

    @Test
    public void doesNotHaveEtagProperty() {
        Map<String, String> headerParams = new HashMap<>();
        assertThat(headerParameterExtractor.hasEtagProperty(headerParams), is(false));
    }
}