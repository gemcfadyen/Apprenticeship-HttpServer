package server.actions;

import org.junit.Before;
import org.junit.Test;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.StatusCode;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class IncludeParametersInBodyTest {

    private final IncludeParametersInBody includeParametersInBody = new IncludeParametersInBody();
    private final Map<String, String> decodedParams = new HashMap<>();
    private HttpRequest httpRequest;

    @Before
    public void setup() {
        decodedParams.put("key1", "value1");
        decodedParams.put("key2", "value2");
        httpRequest = anHttpRequestBuilder()
                .withRequestLine("GET")
                .withRequestUri("/parameters")
                .withParameters(decodedParams)
                .build();
    }

    @Test
    public void isEligibleWhenRequestHasParameters() {
        assertThat(includeParametersInBody.isEligible(httpRequest), is(true));
    }

    @Test
    public void isNotEligibleWhenRequestHasNoHeaderParameters() {
        assertThat(includeParametersInBody.isEligible(anHttpRequestBuilder().build()), is(false));
    }


    @Test
    public void responseHasStatus200() {
        HttpResponse response = includeParametersInBody.process(httpRequest);
        assertThat(response.statusCode(), is(StatusCode.OK));
    }

    @Test
    public void responseBodyContainsParameters() {
        HttpResponse response = includeParametersInBody.process(httpRequest);
        assertThat(response.body(), is("key1 = value1,key2 = value2".getBytes()));
    }
}
