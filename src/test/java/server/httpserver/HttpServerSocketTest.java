package server.httpserver;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.messages.HttpResponseFormatter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerSocketTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ServerSocket fakeServerSocket;
    private HttpServerSocket httpServerSocket;

    @Before
    public void setUp() throws Exception {
        fakeServerSocket = new ServerSocket() {
            @Override
            public Socket accept() throws IOException {
                throw new IOException("Throws exception for test");
            }
        };
        httpServerSocket = new HttpServerSocket(fakeServerSocket, new HttpResponseFormatter());
    }

    @Test
    public void exceptionThrownByServer() throws IOException {
        expectedException.expect(HttpServerSocketException.class);
        expectedException.expectMessage("Exception occurred whilst server was accepting client requests");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(IOException.class));

        httpServerSocket.accept();
    }
}

