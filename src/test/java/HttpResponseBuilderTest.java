import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseBuilderTest {

    @Test
    public void buildsResponseWithStatus200() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatus(200)
                .build();

        assertThat(response.statusCode(), is(200));
    }

    @Test
    public void buildsResponseWithHttpVersion() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withStatus(200)
                .build();

        assertThat(response.httpVersion(), is("HTTP/1.1"));
    }

    @Test
    public void buildsResponseWithReasonPhrase() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withReasonPhrase("OK")
                .build();

        assertThat(response.reasonPhrase(), is("OK"));
    }

    @Test
    public void buildsResponseWithAllowMethods() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder()
                .withAllowMethods("GET", "POST")
                .build();

        assertThat(response.allowedMethods().containsAll(Arrays.asList("GET", "POST")), is(true));
    }
}
