package server.httpserver;

import org.junit.Test;
import server.RequestParserSpy;
import server.RouteProcessorSpy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProcessClientRequestTaskTest {
    private final RequestParserSpy requestParserSpy = new RequestParserSpy();
    private final ClientSpy clientSpy = new ClientSpy();
    private final RouteProcessorSpy routeProcessorSpy = new RouteProcessorSpy();
    private final ProcessClientRequestTask task = new ProcessClientRequestTask(clientSpy, requestParserSpy, routeProcessorSpy);

    @Test
    public void requestIsParsedWhenTaskIsRunning() {
        task.run();

        assertThat(requestParserSpy.hasParsedRequest(), is(true));
    }

    @Test
    public void clientHasHttpResponseOnceProcessed() {
        task.run();

        assertThat(routeProcessorSpy.hasProcessed(), is(true));
        assertThat(clientSpy.hasHttpResponse(), is(true));
    }

    @Test
    public void clientSocketIsClosedAfterItIsProcessed() {
        task.run();

        assertThat(routeProcessorSpy.hasProcessed(), is(true));
        assertThat(clientSpy.isClosed(), is(true));
    }

}
