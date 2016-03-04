package server.messages;

import org.junit.Test;
import server.router.HttpMethods;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseBuilderTest {

    @Test
    public void buildsResponseWithStatus200() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .build();

        assertThat(response.statusCode(), is(StatusCode.OK));
    }

    @Test
    public void buildsResponseWithHttpVersion() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .build();

        assertThat(response.httpVersion(), is("HTTP/1.1"));
    }

    @Test
    public void buildsResponseWithAllowMethods() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withAllowMethods(HttpMethods.GET, HttpMethods.POST)
                .build();

        assertThat(response.allowedMethods().containsAll(Arrays.asList(HttpMethods.GET, HttpMethods.POST)), is(true));
    }

    @Test
    public void buildsResponseWithBody() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .withBody("My=Data".getBytes())
                .build();

        assertThat(response.body(), is("My=Data".getBytes()));
    }

    @Test
    public void buildsResponseWithRedirectLocation() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.FOUND)
                .withLocation("new-url")
                .build();

        assertThat(response.location(), is("new-url"));
    }
}
