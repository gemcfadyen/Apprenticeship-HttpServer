package server.router;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpMethodsTest {

    @Test
    public void isValidMethod() {
        assertThat(HttpMethods.isBogus("PUT"), is(false));
    }

    @Test
    public void isBogusMethod() {
        assertThat(HttpMethods.isBogus("BOGUS_METHOD"), is(true));
    }
}
