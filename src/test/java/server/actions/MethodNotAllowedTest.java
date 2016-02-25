package server.actions;

import org.junit.Test;
import server.HttpMethods;
import server.StatusCode;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class MethodNotAllowedTest {

   private MethodNotAllowed methodNotAllowed =  new MethodNotAllowed();

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
