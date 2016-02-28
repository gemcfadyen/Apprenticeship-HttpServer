package server;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpServerTest {

    private ServerSocketSpy serverSocketSpy;
    private RequestParserSpy httpRequestParserSpy;
    private HttpServer httpServer;
    private ClientSpy clientSpy;
    private RouteProcessorSpy httpRequestProcessorSpy;

    @Before
    public void setUp() throws Exception {
        clientSpy = new ClientSpy();
        serverSocketSpy = new ServerSocketSpy(clientSpy);
        httpRequestParserSpy = new RequestParserSpy();
        httpRequestProcessorSpy = new RouteProcessorSpy();
        httpServer = new HttpServer(serverSocketSpy, httpRequestParserSpy, httpRequestProcessorSpy);
    }

    @Test
    public void whenServerIsStartedItAcceptsClientRequests() {
        httpServer.processRequest();

        assertThat(serverSocketSpy.isAcceptingRequests(), is(true));
    }
}
