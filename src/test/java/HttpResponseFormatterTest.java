import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseFormatterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ResponseFormatter formatter;
    private HttpResponseFormatter responseFormatterThrowingException;

    @Before
    public void setup() {
        formatter = new HttpResponseFormatter();
        responseFormatterThrowingException = new HttpResponseFormatter() {
            @Override
            protected byte[] createFormatted(HttpResponse response) throws UnsupportedEncodingException {
                throw new UnsupportedEncodingException("Exception Thrown For Test");
            }
        };
    }

    @Test
    public void statusLineResponseFormat() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder().withStatus(200).withReasonPhrase("OK").build();

        byte[] formattedResponse = formatter.format(response);

        assertThat(formattedResponse, is("HTTP/1.1 200 OK\r\n\r\n".getBytes()));
    }

    @Test
    public void statusLineWithAllowMethodsResponseFormat() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder().withStatus(200).withReasonPhrase("OK").withAllowMethods(HttpMethods.GET, HttpMethods.PUT).build();

        byte[] formattedResponse = formatter.format(response);

        assertThat(formattedResponse, is("HTTP/1.1 200 OK\r\nAllow: GET,PUT\r\n\r\n".getBytes()));
    }

    @Test
    public void locationLineResponseFormat() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder().withStatus(302).withReasonPhrase("Found").withLocation("www.newurl.com").build();

        byte[] formattedResponse = formatter.format(response);

        assertThat(formattedResponse, is("HTTP/1.1 302 Found\r\nLocation: www.newurl.com\r\n\r\n".getBytes()));
    }

    @Test
    public void bodyIncludedOnResponse() {
        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder().withStatus(200).withReasonPhrase("OK").withBody("My=Data").build();

        byte[] formattedResponse = formatter.format(response);

        assertThat(formattedResponse, is("HTTP/1.1 200 OK\r\n\r\nMy=Data".getBytes()));
    }

    @Test
    public void exceptionOnFormatting() {
        expectedException.expect(HttpResponseException.class);
        expectedException.expectMessage("Error in creating HTTP Response");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(UnsupportedEncodingException.class));

        HttpResponse response = HttpResponseBuilder.anHttpResponseBuilder().withStatus(200).withReasonPhrase("OK").withAllowMethods(HttpMethods.GET, HttpMethods.PUT).build();
        responseFormatterThrowingException.format(response);
    }
}