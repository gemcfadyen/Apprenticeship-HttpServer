package server.httpserver;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ClientSocketExceptionTest {
    private IOException originalException;
    private ClientSocketException exception;

    @Before
    public void setUp() throws Exception {
        originalException = new IOException();
        exception = new ClientSocketException("Error in Client Socket", originalException);
    }

    @Test
    public void clientSocketExceptionHasMessage() {
        assertThat(exception.getMessage(), is("Error in Client Socket"));
    }

    @Test
    public void clientSocketExceptionCause() {
        assertThat(exception.getCause(), is(originalException));
    }
}
