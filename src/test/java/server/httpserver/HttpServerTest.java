package server.httpserver;

import org.junit.Before;
import org.junit.Test;
import server.RequestParserSpy;
import server.RouteProcessorSpy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpServerTest {

    private ServerSocketSpy serverSocketSpy;
    private HttpServer httpServer;
    private ExecutorServiceFactorySpy executorServiceFactorySpy;
    private ExecutorServiceSpy executorServiceSpy;

    @Before
    public void setUp() throws Exception {
        serverSocketSpy = new ServerSocketSpy(new ClientSpy());
        executorServiceSpy = new ExecutorServiceSpy();
        executorServiceFactorySpy = new ExecutorServiceFactorySpy(executorServiceSpy);

        httpServer = new HttpServer(serverSocketSpy,
                new RequestParserSpy(),
                new RouteProcessorSpy(),
                executorServiceFactorySpy
        );
    }

    @Test
    public void whenServerIsProcessingItCreatesThreadpool() {
        httpServer.processRequest();

        assertThat(executorServiceFactorySpy.hasInitialisedThreadPool(), is(true));
    }

    @Test
    public void whenServerIsProcessingItExecutesRunnableTask() {
        httpServer.processRequest();

        assertThat(serverSocketSpy.isAcceptingRequests(), is(true));
        assertThat(executorServiceSpy.hasExecuted(), is(true));
    }

    @Test
    public void threadExecutorServiceShutsdown() {
        httpServer.processRequest();

        assertThat(executorServiceSpy.hasShutdodwn(), is(true));
    }
}
