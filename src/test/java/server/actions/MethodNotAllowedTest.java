package server.actions;

import org.junit.Test;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;
import server.router.HttpMethods;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpMessageHeaderProperties.CONTENT_LENGTH;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class MethodNotAllowedTest {

    private MethodNotAllowed methodNotAllowed = new MethodNotAllowed(new HeaderParameterExtractor());

    @Test
    public void eligibleWhenContentLengthIsZero() {
        Map<String, String> headerProperties = new HashMap<>();
        headerProperties.put(CONTENT_LENGTH.name(), "0");

        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestLine(HttpMethods.PUT.name())
                .withHeaderParameters(headerProperties)
                .build();

        assertThat(methodNotAllowed.isEligible(httpRequest), is(true));
    }

    @Test
    public void statusCode405Returned() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestLine(HttpMethods.PUT.name())
                .build();

        HttpResponse httpResponse = methodNotAllowed.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(StatusCode.METHOD_NOT_ALLOWED));
    }

    @Test
    public void allowMethodsAreIncludedOnResponse() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestLine(HttpMethods.PUT.name())
                .build();

        HttpResponse httpResponse = methodNotAllowed.process(httpRequest);

        assertThat(httpResponse.allowedMethods(), containsInAnyOrder(HttpMethods.values()));
    }
}
