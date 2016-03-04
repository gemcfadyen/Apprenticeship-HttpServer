package server;

import server.messages.HttpResponse;

public interface ResponseFormatter {
    byte[] format(HttpResponse response);
}
