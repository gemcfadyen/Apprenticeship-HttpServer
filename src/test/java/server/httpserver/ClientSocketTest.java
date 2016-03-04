package server.httpserver;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.ResponseFormatter;
import server.messages.HttpResponse;
import server.messages.HttpResponseBuilder;
import server.messages.HttpResponseFormatter;
import server.messages.StatusCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ClientSocketTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Socket socketThatThrowsExceptions;
    private ClientSocket clientSocket;


    @Before
    public void setUp() throws Exception {
        setupSocketThatThrowsExceptions();
        clientSocket = new ClientSocket(socketThatThrowsExceptions, new HttpResponseFormatter());
    }

    @Test
    public void exceptionThrownWhenErrorInGettingResponse() {
        expectedException.expect(ClientSocketException.class);
        expectedException.expectMessage("Exception in socket whilst retrieving the request");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(IOException.class));

        clientSocket.getRawHttpRequest();
    }

    @Test
    public void exceptionThrownWhenErrorInClosingSocket() {
        expectedException.expect(ClientSocketException.class);
        expectedException.expectMessage("Exception whilst closing socket");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(IOException.class));

        clientSocket.close();
    }

    @Test
    public void exceptionThrownWhenErrorInSettingResponse() {
        expectedException.expect(ClientSocketException.class);
        expectedException.expectMessage("Exception whilst writing request to socket");
        expectedException.expectCause(IsInstanceOf.<Throwable>instanceOf(IOException.class));

        HttpResponse httpResponse = HttpResponseBuilder.anHttpResponseBuilder().withStatusCode(StatusCode.OK).build();
        clientSocket.setHttpResponse(httpResponse);
    }

    @Test
    public void responseIsFormattedWhenSentToClient() {
        FormatterSpy responseFormatterSpy = new FormatterSpy();

        ClientSocket clientSocket = new ClientSocket(fakeSocket(), responseFormatterSpy);

        clientSocket.setHttpResponse(HttpResponseBuilder.anHttpResponseBuilder()
                .withStatusCode(StatusCode.OK)
                .build());

        assertThat(responseFormatterSpy.hasFormatted(), is(true));
    }

    private void setupSocketThatThrowsExceptions() {
        socketThatThrowsExceptions = new Socket() {
            @Override
            public InputStream getInputStream() throws IOException {
                throw new IOException("Exception thrown for testing");
            }

            public synchronized void close() throws IOException {
                throw new IOException("Exception thrown for testing");
            }

            public OutputStream getOutputStream() throws IOException {
                throw new IOException("Exception thrown for testing");

            }
        };
    }

    private Socket fakeSocket() {
        return new Socket() {
            @Override
            public InputStream getInputStream() throws IOException {
                throw new RuntimeException("Not implemented for test");
            }

            public synchronized void close() throws IOException {
                throw new RuntimeException("Not implemented for test");
            }

            public OutputStream getOutputStream() throws IOException {
                return new ByteArrayOutputStream(10);
            }
        };
    }
}

class FormatterSpy implements ResponseFormatter {
    private boolean hasFormatted = false;

    @Override
    public byte[] format(HttpResponse response) {
        hasFormatted = true;
        return new byte[0];
    }

    public boolean hasFormatted() {
        return hasFormatted;
    }
}
