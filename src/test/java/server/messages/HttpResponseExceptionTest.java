package server.messages;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseExceptionTest {

    private IOException originalException;
    private HttpResponseException exception;

    @Before
    public void setUp() throws Exception {
        originalException = new IOException();
        exception = new HttpResponseException(originalException);
    }

    @Test
    public void httpResponseExceptionHasMessage() {
        assertThat(exception.getMessage(), is("Error when creating Http Response"));
    }

    @Test
    public void httpResponseExceptionCause() {
        assertThat(exception.getCause(), is(originalException));

    }
}
