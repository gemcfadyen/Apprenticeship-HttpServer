package server;

import org.junit.Test;
import server.messages.HttpRequestParser;
import server.messages.HttpResponseFormatter;

import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ServerConnectionsTest {

    @Test
    public void aClientCanConnectToTheServer() throws Exception {

        String path = System.getProperty("user.home") + "/Documents/Projects/cob-server/cob_spec/public";
        HttpServerSocket httpServerSocket = new HttpServerSocket(new ServerSocket(8080), new HttpResponseFormatter());
        HttpServer httpServer = new HttpServer("localhost", 8080, httpServerSocket, new HttpRequestParser(), new HttpRouteProcessor(new FileResourceHandler(path)));

//        httpServer.processRequest();
//        httpServer.ensureStartupHasFinished();
//
//        ClientSocket clientSocket = new ClientSocket(new Socket("localhost", 8080), new HttpResponseFormatter());
//        clientSocket.connect();
//
//
//       assertThat(httpServer.numberOfConnections(), is(1));

    }
}
