package server.router;

import server.HttpMethods;

public class RoutingCriteria {
    private final Route route;
    private final HttpMethods method;

    public RoutingCriteria(Route route, HttpMethods method) {
        this.route = route;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoutingCriteria routingCriteria = (RoutingCriteria) o;

        if (route != null ? !route.equals(routingCriteria.route) : routingCriteria.route != null) return false;
        return !(method != null ? !method.equals(routingCriteria.method) : routingCriteria.method != null);

    }

    @Override
    public int hashCode() {
        int result = route != null ? route.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}
