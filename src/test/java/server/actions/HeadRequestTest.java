package server.actions;

import org.junit.Test;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.router.HttpMethods;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class HeadRequestTest {

    @Test
    public void isEligible() {
        HeadRequest headRequest = new HeadRequest();

        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestLine("/aUri")
                .withRequestLine(HttpMethods.HEAD.name())
                .build();

        assertThat(headRequest.isEligible(httpRequest), is(true));
    }

    @Test
    public void responseHasNoBody() {
        HeadRequest headRequest = new HeadRequest();

        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestLine("/aUri")
                .withRequestLine(HttpMethods.HEAD.name())
                .build();

        HttpResponse httpResponse = headRequest.process(httpRequest);

        assertThat(httpResponse.body(), nullValue());
    }

}