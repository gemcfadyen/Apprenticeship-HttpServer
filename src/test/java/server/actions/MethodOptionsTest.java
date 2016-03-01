package server.actions;

import org.junit.Test;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.OK;
import static server.HttpMethods.*;

public class MethodOptionsTest {
    private MethodOptions methodOptions = new MethodOptions();

    @Test
    public void simpleOptionReturns200Code() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/method_options")
                .withRequestLine(OPTIONS.name())
                .build();

        HttpResponse httpResponse = methodOptions.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(OK));
    }

    @Test
    public void simpleOptionReturnsMethodsInAllow() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/method_options")
                .withRequestLine(OPTIONS.name())
                .build();

        HttpResponse httpResponse = methodOptions.process(httpRequest);

        assertThat(httpResponse.allowedMethods(), containsInAnyOrder(GET, HEAD, POST, OPTIONS, PUT, DELETE, PATCH));
    }

}
