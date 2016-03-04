package server.router;

import server.ResourceHandler;
import server.messages.HttpRequest;

import static server.router.Resource.LOGS;

public class RouteLog implements Auditor {
    private ResourceHandler resourceHandler;

    public RouteLog(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void audit(HttpRequest request) {
        String resourceContent = String.format("%s %s %s\n", request.getMethod(), request.getRequestUri(), request.getProtocolVersion());
        resourceHandler.append(LOGS.getPath(), resourceContent);
    }
}
