package server.messages;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.Map;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpRequestParserTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private InputStream getRequestInputStream;
    private InputStream getWithParametersInputStream;
    private InputStream postRequestInputStream;

    private HttpRequestParser parser;

    @Before
    public void setupFixture() {
        getRequestInputStream = setupGetRequest();
        postRequestInputStream = setupPostRequest();
        getWithParametersInputStream = setupGetWithParametersRequest();

        parser = new HttpRequestParser();
    }

    @Test
    public void parsesMethodFromRequest() {
        HttpRequest request = parser.parse(getRequestInputStream);

        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void exceptionThrownOnParseError() {
        expectedException.expect(HttpRequestParsingException.class);
        expectedException.expectMessage("Error when parsing Http Request");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(IOException.class));

        BufferedReaderWhichThrowsExceptionOnReadLine readerToThrowException = new BufferedReaderWhichThrowsExceptionOnReadLine(new StringReader("hi"));
        parser.parseRequest(readerToThrowException);
    }

    @Test
    public void parsesUriFromRequest() {
        HttpRequest request = parser.parse(getRequestInputStream);

        assertThat(request.getRequestUri(), is("/"));
    }

    @Test
    public void parsesMapOfHeaderProperties() {
        HttpRequest request = parser.parse(postRequestInputStream);
        Map<String, String> headerProperties = request.headerParameters();

        assertThat(headerProperties.get("From"), is("frog@jmarshall.com"));
        assertThat(headerProperties.get("User-Agent"), is("HTTPTool/1.1"));
        assertThat(headerProperties.get("Content-Type"), is("application/x-www-form-urlencoded"));
        assertThat(headerProperties.get("Content-Length"), is("32"));
    }

    @Test
    public void parsesBodyFromPostRequest() {
        HttpRequest request = parser.parse(postRequestInputStream);

        assertThat(request.getBody(), is("home=Cosby&favorite+flavor=flies"));
    }

    @Test
    public void parsesHeaderContainingParameters() {
        HttpRequest request = parser.parse(getWithParametersInputStream);

        assertThat(request.getRequestUri(), is("/parameters"));
        Map<String, String> requestParams = request.params();
        assertThat(requestParams.size(), is(2));
        assertThat(requestParams.get("variable_1"), is("Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?"));
        assertThat(requestParams.get("variable_2"), is("stuff"));
    }

    @Test
    public void exceptionIsThrownWhenErrorInDecodingParameters() {
        expectedException.expect(HttpRequestParsingException.class);
        expectedException.expectMessage("Error when parsing Http Request");
        expectedException.expectCause(instanceOf(UnsupportedEncodingException.class));

        HttpRequestParser parserWhichThrowsExceptionOnDecoding = new HttpRequestParser() {
            protected String decodeUsingUtf8(String parameter) throws UnsupportedEncodingException {
                throw new UnsupportedEncodingException("Throws exception for test");
            }
        };

        parserWhichThrowsExceptionOnDecoding.getRequestParams(new String[]{"GET", "/routeToResource?key=value&otherKey=otherValue"});
    }

    private InputStream setupGetWithParametersRequest() {
        String getWithParameters = "GET /parameters?variable_1=Operators%20%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n\r\n";

        return new ByteArrayInputStream(getWithParameters.getBytes());
    }

    private InputStream setupPostRequest() {
        String postFixture = "POST /path/script.cgi HTTP/1.1\r\n" +
                "From: frog@jmarshall.com\r\n" +
                "User-Agent: HTTPTool/1.1\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 32\r\n\r\nhome=Cosby&favorite+flavor=flies";
        return new ByteArrayInputStream(postFixture.getBytes());
    }

    private InputStream setupGetRequest() {
        String getRequest = "GET / HTTP/1.1\r\n" +
                "Host: localhost:5000\r\n" +
                "Connection: Keep-Alive\r\n" +
                "User-Agent: Apache-HttpClient/4.3.5 (java 1.5)\r\n" +
                "Accept-Encoding: gzip,deflate\r\n\r\n";
        return new ByteArrayInputStream(getRequest.getBytes());
    }
}

class BufferedReaderWhichThrowsExceptionOnReadLine extends BufferedReader {

    public BufferedReaderWhichThrowsExceptionOnReadLine(Reader in) {
        super(in);
    }

    public String readLine() throws IOException {
        throw new IOException("Throws Exception for test");
    }
}
