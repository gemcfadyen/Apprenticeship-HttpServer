package server.router;

import server.messages.HttpRequest;

public interface Auditor {
    void audit(HttpRequest request);
}
