package server.router;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static server.HttpMethods.GET;
import static server.HttpMethods.POST;
import static server.router.Route.FILE;
import static server.router.Route.HOME;

public class RoutingCriteriaTest {

    @Test
    public void routingKeysAreEqual() {
        RoutingCriteria routingKey1 = new RoutingCriteria(HOME, GET);
        RoutingCriteria routingKey2 = new RoutingCriteria(HOME, GET);

        assertThat(routingKey1, is(equalTo(routingKey2)));
        assertThat(routingKey1.hashCode(), is(equalTo(routingKey2.hashCode())));
    }

    @Test
    public void routeKeysWithDifferentMethodsNotEqual() {
        RoutingCriteria routingKey1 = new RoutingCriteria(HOME, GET);
        RoutingCriteria routingKey2 = new RoutingCriteria(HOME, POST);

        assertThat(routingKey1, not(equalTo(routingKey2)));
        assertThat(routingKey1.hashCode(), not(equalTo(routingKey2.hashCode())));
    }

    @Test
    public void routeKeysWithDifferentRoutesAreNotEqual() {
        RoutingCriteria routingKey1 = new RoutingCriteria(FILE, GET);
        RoutingCriteria routingKey2 = new RoutingCriteria(HOME, GET);

        assertThat(routingKey1, not(equalTo(routingKey2)));
        assertThat(routingKey1.hashCode(), not(equalTo(routingKey2.hashCode())));
    }
}
