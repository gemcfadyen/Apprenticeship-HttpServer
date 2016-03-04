package server.router;

import server.messages.HttpRequest;

public class RouteLogSpy implements Auditor {
    private boolean hasLogged = false;

    @Override
    public void audit(HttpRequest request) {
        hasLogged = true;
    }

    public boolean hasLoggedRequest() {
        return hasLogged;
    }
}
