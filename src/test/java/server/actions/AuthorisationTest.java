package server.actions;

import org.junit.Test;
import server.ResourceHandler;
import server.ResourceHandlerSpy;
import server.StatusCode;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.HttpResponseBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.messages.HttpMessageHeaderProperties.AUTHORISATION;

public class AuthorisationTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private ReadResourceSpy readResourceSpy = new ReadResourceSpy(resourceHandlerSpy);
    private Authorisation authorisation = new Authorisation(readResourceSpy);

    @Test
    public void returns403WhenRequestDoesNotContainAuthorisationFields() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(new HashMap<>())
                .build();

        HttpResponse httpResponse = authorisation.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(StatusCode.UNAUTHORISED));
    }

    @Test
    public void returnsResponseAskingForAuthentication() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(new HashMap<>())
                .build();

        HttpResponse httpResponse = authorisation.process(httpRequest);

        assertThat(httpResponse.authorisationRequest(), is(true));
    }

    @Test
    public void authenticatesIfRequestContainsCredentials() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AUTHORISATION.getPropertyName(), "Basic YWRtaW46aHVudGVyMg==");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(parameters)
                .build();

        authorisation.process(httpRequest);

        assertThat(readResourceSpy.hasProcessedReadResouce(), is(true));
    }

    @Test
    public void requestNotAuthorisedIfIncorrectCredentialsSupplied() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AUTHORISATION.getPropertyName(), "Something-Wrong");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(parameters)
                .build();

        authorisation.process(httpRequest);

        assertThat(readResourceSpy.hasProcessedReadResouce(), is(false));
    }

    @Test
    public void requestNotAuthorisedIfExceptionThrownWhenDecrypting() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AUTHORISATION.getPropertyName(), "YWRtaW46aHVudGVyMg==");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(parameters)
                .build();
        authorisation = new Authorisation(readResourceSpy) {
            protected boolean decode(byte[] bytes) {
                throw new RuntimeException("Exception thrown for test");
            }
        };

        authorisation.process(httpRequest);

        assertThat(readResourceSpy.hasProcessedReadResouce(), is(false));
    }
}

class ReadResourceSpy extends ReadResource {
    private boolean hasProcessedRead;

    public ReadResourceSpy(ResourceHandler resourceHandler) {
        super(resourceHandler);
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        hasProcessedRead = true;
        return HttpResponseBuilder.anHttpResponseBuilder().build();
    }

    public boolean hasProcessedReadResouce() {
        return hasProcessedRead;
    }
}
