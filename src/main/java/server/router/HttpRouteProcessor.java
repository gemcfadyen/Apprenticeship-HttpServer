package server.router;

import server.Action;
import server.HttpMethods;
import server.RouteProcessor;
import server.actions.MethodNotAllowed;
import server.actions.UnknownRoute;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static server.HttpMethods.isBogus;

public class HttpRouteProcessor implements RouteProcessor {
    private Map<RoutingCriteria, Action> routes = new HashMap<>();

    public HttpRouteProcessor(Routes routes) {
        this.routes = routes.routes();
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        if (isBogusMethod(httpRequest)) {
            return methodNotSupported(httpRequest);
        } else if (supportedRoute(httpRequest)) {
            return processRoute(httpRequest);
        } else {
            return fourOFour(httpRequest);
        }
    }

    private boolean isBogusMethod(HttpRequest httpRequest) {
        return isBogus(httpRequest.getMethod());
    }

    private HttpResponse methodNotSupported(HttpRequest httpRequest) {
        return new MethodNotAllowed().process(httpRequest);
    }

    private boolean supportedRoute(HttpRequest httpRequest) {
        return Route.isSupported(httpRequest.getRequestUri())
                && routes.get(routingCriteria(httpRequest)) != null;
    }

    private HttpResponse processRoute(HttpRequest httpRequest) {
        return routes.get(routingCriteria(httpRequest)).process(httpRequest);
    }

    private RoutingCriteria routingCriteria(HttpRequest httpRequest) {
        return new RoutingCriteria(Route.getRouteFor(httpRequest.getRequestUri()), HttpMethods.valueOf(httpRequest.getMethod()));
    }

    private HttpResponse fourOFour(HttpRequest httpRequest) {
        return new UnknownRoute().process(httpRequest);
    }
}
