package server;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpServerTest {
    private ServerSocketSpy serverSocketSpy;
    private HttpServer httpServer;
    private ExecutorServiceFactorySpy executorServiceFactorySpy;
    private ThreadExecutorServiceSpy threadExecutorServiceSpy;

    @Before
    public void setUp() throws Exception {
        serverSocketSpy = new ServerSocketSpy(new ClientSpy());
        threadExecutorServiceSpy = new ThreadExecutorServiceSpy();
        executorServiceFactorySpy = new ExecutorServiceFactorySpy(threadExecutorServiceSpy);

        httpServer = new HttpServer(
                serverSocketSpy,
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
        assertThat(threadExecutorServiceSpy.hasExecutedRunnable(), is(true));
    }

    @Test
    public void threadExecutorServiceShutsdown() {
        httpServer.processRequest();

        assertThat(threadExecutorServiceSpy.hasShutdown(), is(true));
    }
}