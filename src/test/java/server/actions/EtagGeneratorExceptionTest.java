package server.actions;

import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EtagGeneratorExceptionTest {
    private NoSuchAlgorithmException originalException;
    private EtagGeneratorException exception;

    @Before
    public void setUp() throws Exception {
        originalException = new NoSuchAlgorithmException("no sha 1 algorithm");
        exception = new EtagGeneratorException("Error in Etag Generation", originalException);
    }

    @Test
    public void etagGeneratorExceptionHasAMessage() {
        assertThat(exception.getMessage(), is("Error in Etag Generation"));
    }

    @Test
    public void etagGeneratorExceptionCause() {
        assertThat(exception.getCause(), is(originalException));
    }

}
