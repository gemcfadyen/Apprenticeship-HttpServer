package server.router;

import server.Action;
import server.HttpMethods;
import server.RouteProcessor;
import server.actions.MethodNotAllowed;
import server.actions.NoDefaultActionException;
import server.actions.UnknownRoute;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.HttpMethods.isBogus;

public class HttpRouteProcessor implements RouteProcessor {
    private Map<HttpMethods, List<Action>> routes = new HashMap<>();

    public HttpRouteProcessor(Routes routes) {
        this.routes = routes.routes();
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        System.out.println("Request received from " + httpRequest.getRequestUri() + " with verb " + httpRequest.getMethod());
        if (isBogusMethod(httpRequest)) {
            return methodNotSupported(httpRequest);
        } else {//if (supportedRoute(httpRequest)) {
            return processRoute(httpRequest);
        }
        //else {
        //    return fourOFour(httpRequest);
       // }
    }

    private boolean isBogusMethod(HttpRequest httpRequest) {
        return isBogus(httpRequest.getMethod());
    }

    private HttpResponse methodNotSupported(HttpRequest httpRequest) {
        return new MethodNotAllowed().process(httpRequest);
    }

    private boolean supportedRoute(HttpRequest httpRequest) {
        return routes.get(HttpMethods.valueOf(httpRequest.getMethod())) != null;
    }

    private HttpResponse processRoute(HttpRequest httpRequest) {
        List<Action> actions = routes.get(HttpMethods.valueOf(httpRequest.getMethod()));//routes.get(routingCriteria(httpRequest));
        for (Action action : actions) {
            if (action.isEligible(httpRequest)) {
                return action.process(httpRequest);
            }
        }
        throw new NoDefaultActionException();
    }

    private HttpResponse fourOFour(HttpRequest httpRequest) {
        return new UnknownRoute().process(httpRequest);
    }
}
