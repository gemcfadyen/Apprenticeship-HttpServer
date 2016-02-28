package server;

import server.messages.HttpRequestParser;
import server.messages.HttpResponseFormatter;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServerRunner {

    public static void main(String... args) throws IOException {
        CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser(System.getProperty("user.home"));

        String host = "localhost";
        int port = commandLineArgumentParser.extractPort(args);
        String publicDirectory = commandLineArgumentParser.extractPublicDirectory(args);

        HttpServerSocket httpServerSocket = new HttpServerSocket(new ServerSocket(port), new HttpResponseFormatter());

        HttpServer httpServer = new HttpServer(
                host,
                port,
                httpServerSocket,
                new HttpRequestParser(),
                new HttpRouteProcessor(new FileResourceHandler(publicDirectory))
        );

        start(httpServer);
    }

    private static void start(HttpServer server) {
        while (true) {
            server.processRequest();
        }
    }
}
