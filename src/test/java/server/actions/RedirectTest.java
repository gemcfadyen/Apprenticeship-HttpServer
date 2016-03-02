package server.actions;

import org.junit.Test;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.StatusCode.FOUND;
import static server.HttpMethods.GET;

public class RedirectTest {
    private Redirect redirect = new Redirect();

    @Test
    public void isEligibleForUriRedirect() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/redirect")
                .withRequestLine(GET.name())
                .build();

        assertThat(redirect.isEligible(httpRequest), is(true));
    }

    @Test
    public void isNotEligibleForOtherUris() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/another")
                .withRequestLine(GET.name())
                .build();

        assertThat(redirect.isEligible(httpRequest), is(false));
    }

    @Test
    public void redirectReturns302() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/redirect")
                .withRequestLine(GET.name())
                .build();

        HttpResponse httpResponse = redirect.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(FOUND));
        assertThat(httpResponse.location(), is("http://localhost:5000/"));
    }
}
