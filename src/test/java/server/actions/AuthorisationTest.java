package server.actions;

import org.junit.Test;
import server.ResourceHandler;
import server.ResourceHandlerSpy;
import server.messages.*;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpMessageHeaderProperties.AUTHORISATION;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;
import static server.router.HttpMethods.GET;

public class AuthorisationTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private ReadResourceSpy readResourceSpy = new ReadResourceSpy(resourceHandlerSpy);
    private Authorisation authorisation = new Authorisation(readResourceSpy, new HeaderParameterExtractor());

    @Test
    public void actionIsEligibleWhenRequestIsForLogs() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/logs")
                .withRequestLine(GET.name())
                .withHeaderParameters(new HashMap<>())
                .build();
        assertThat(authorisation.isEligible(httpRequest), is(true));
    }

    @Test
    public void actionIsEligibleWhenRequestHasAuthorisationHeader() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AUTHORISATION.getPropertyName(), "Basic YWRtaW46aHVudGVyMg==");
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/another-uri-with-authentication")
                .withRequestLine(GET.name())
                .withHeaderParameters(parameters)
                .build();

        assertThat(authorisation.isEligible(httpRequest), is(true));
    }

    @Test
    public void actionIsNotEligibleIfItIsNotLogsAndDoesNotContainAuthorisationHeaders() {
        HttpRequest httpRequest = anHttpRequestBuilder()
                .withRequestUri("/something-else")
                .withRequestLine(GET.name())
                .withHeaderParameters(new HashMap<>())
                .build();
        assertThat(authorisation.isEligible(httpRequest), is(false));
    }

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
        authorisation = new Authorisation(readResourceSpy, new HeaderParameterExtractor()) {
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
