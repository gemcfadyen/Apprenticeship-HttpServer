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
        httpServer = new HttpServer("localhost", 8080, serverSocketSpy, httpRequestParserSpy, httpRequestProcessorSpy);
    }

    @Test
    public void serverHasAHost() {
        assertThat(httpServer.getHost(), is("localhost"));
    }

    @Test
    public void serverHasAPort() {
        assertThat(httpServer.getPort(), is(8080));
    }

    @Test
    public void whenServerIsStartedItAcceptsClientRequests() {
        httpServer.processRequest();

        assertThat(serverSocketSpy.isAcceptingRequests(), is(true));
    }

//    @Test
//    public void serverParsesClientRequest() {
//        httpServer.processRequest();
//
//        assertThat(httpRequestParserSpy.hasParsedRequest(), is(true));
//    }

//    @Test
//    public void serverProcessesRequest() {
//        httpServer.processRequest();
//
//        assertThat(httpRequestProcessorSpy.hasProcessed(), is(true));
//    }

//    @Test
//    public void providesClientWithHttpResponse() {
//        httpServer.processRequest();
//
//        assertThat(clientSpy.hasHttpResponse(), is(true));
//    }

//    @Test
//    public void clientConnectionIsClosed() {
//        httpServer.processRequest();
//
//        assertThat(clientSpy.isClosed(), is(true));
//    }
}
