package server;

import server.messages.HttpRequest;

import java.io.InputStream;

public interface RequestParser {
    HttpRequest parse(InputStream inputStream);
}
