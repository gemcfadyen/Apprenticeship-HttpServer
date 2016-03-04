package server;

import server.messages.HttpRequest;

import java.io.InputStream;

public class RequestParserSpy implements RequestParser {
    private boolean hasParsedRequest = false;

    @Override
    public HttpRequest parse(InputStream inputStream) {
        hasParsedRequest = true;
        return null;
    }

    public boolean hasParsedRequest() {
        return hasParsedRequest;
    }
}
