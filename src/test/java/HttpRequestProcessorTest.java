import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.EMPTY_MAP;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpRequestProcessorTest {
    private HttpRequestProcessor requestProcessor;
    private ResourceFinderSpy resourceFinderSpy;
    private ResourceHandlerSpy resourceWriterSpy;

    @Before
    public void setup() {
        resourceFinderSpy = new ResourceFinderSpy();
        resourceWriterSpy = new ResourceHandlerSpy();
        requestProcessor = new HttpRequestProcessor(resourceFinderSpy, resourceWriterSpy);
    }

    @Test
    public void provides404WhenNoRoutesMet() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET.name(), "/unknown/route", EMPTY_MAP, "");

        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(404));
    }

    @Test
    public void simpleGetReturnsCode200() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET.name(), "/", EMPTY_MAP, "");

        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
    }

    @Test
    public void simplePutReturnsCode200() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST.name(), "/form", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
    }

    @Test
    public void simpleOptionReturns200Code() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.OPTIONS.name(), "/method_options", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
    }

    @Test
    public void simpleOptionReturnsMethodsInAllow() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.OPTIONS.name(), "/method_options", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.allowedMethods(), containsInAnyOrder(HttpMethods.GET, HttpMethods.HEAD, HttpMethods.POST, HttpMethods.OPTIONS, HttpMethods.PUT, HttpMethods.DELETE));
    }

    @Test
    public void getMethodLooksUpResourceForResponseBody() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET.name(), "/form", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
        assertThat(httpResponse.body(), is("My=Data"));
        assertThat(resourceFinderSpy.hasLookedupResource(), is(true));
    }

    @Test
    public void postMethodCreatesAResource() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST.name(), "/form", EMPTY_MAP, "content");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
        assertThat(httpResponse.body(), is("content"));
        assertThat(resourceWriterSpy.hasWrittenToResource(), is(true));
    }

    @Test
    public void putMethodUpdatesAResource() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.PUT.name(), "/form", EMPTY_MAP, "content");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
        assertThat(httpResponse.body(), is("content"));
        assertThat(resourceWriterSpy.hasWrittenToResource(), is(true));
    }

    @Test
    public void deleteMethodRemovesResource() {
        HttpRequest httpRequest = new HttpRequest(HttpMethods.DELETE.name(), "/form", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(200));
        assertThat(resourceWriterSpy.hasDeletedResource(), is(true));
    }

    @Test
    public void redirectReturns302() {
//        HTTP/1.1 302 Found
//        Location: http://www.iana.org/domains/example/

        HttpRequest httpRequest = new HttpRequest(HttpMethods.GET.name(), "/redirect", EMPTY_MAP, "");
        HttpResponse httpResponse = requestProcessor.process(httpRequest);

        assertThat(httpResponse.statusCode(), is(302));
        assertThat(httpResponse.location(), is("http://localhost:5000/"));
    }

}
