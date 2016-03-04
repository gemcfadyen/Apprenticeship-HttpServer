package server.router;

import org.junit.Test;
import server.Action;
import server.ResourceHandlerSpy;
import server.actions.*;
import server.messages.HeaderParameterExtractor;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.router.HttpMethods.*;

public class RoutesTest {
    private ResourceHandlerSpy resourceHandlerSpy = new ResourceHandlerSpy();
    private HeaderParameterExtractor headerParameterExtractor = new HeaderParameterExtractor();
    private Routes routes = new Routes(resourceHandlerSpy, headerParameterExtractor);

    @Test
    public void routesForSevenMethods() {
        assertThat(routes.routes().size(), is(7));
    }

    @Test
    public void getHasSevenRoutes() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();
        assertThat(routes.get(GET).size(), is(7));
    }

    @Test
    public void getHasReadResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(GET);

        Action lastAction = actions.get(6);
        assertThat(lastAction, instanceOf(ReadResource.class));
    }

    @Test
    public void postHasTwoRoutes() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(POST);

        assertThat(actions.size(), is(2));
    }

    @Test
    public void postHasWriteResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(POST);

        Action lastAction = actions.get(1);
        assertThat(lastAction, instanceOf(WriteResource.class));
    }

    @Test
    public void putHasTwoRoutes() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(PUT);

        assertThat(actions.size(), is(2));
    }

    @Test
    public void putHasWriteResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(PUT);

        Action lastAction = actions.get(1);
        assertThat(lastAction, instanceOf(WriteResource.class));
    }

    @Test
    public void deleteHasOneAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(DELETE);

        assertThat(actions.size(), is(1));
    }

    @Test
    public void deleteHasDeleteResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(DELETE);

        Action lastAction = actions.get(0);
        assertThat(lastAction, instanceOf(DeleteResource.class));
    }

    @Test
    public void optionHasOneRoute() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(OPTIONS);

        assertThat(actions.size(), is(1));
    }

    @Test
    public void optionHasReadResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(OPTIONS);

        Action lastAction = actions.get(0);
        assertThat(lastAction, instanceOf(ReadResource.class));
    }

    @Test
    public void patchHasOneRoute() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(PATCH);

        assertThat(actions.size(), is(1));
    }

    @Test
    public void patchHasReadResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(PATCH);

        Action lastAction = actions.get(0);
        assertThat(lastAction, instanceOf(PatchResource.class));
    }

    @Test
    public void headHasOneRoute() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(HEAD);

        assertThat(actions.size(), is(1));
    }

    @Test
    public void headHasReadResourceAsFinalAction() {
        Map<HttpMethods, List<Action>> routes = this.routes.routes();

        List<Action> actions = routes.get(HEAD);

        Action lastAction = actions.get(0);
        assertThat(lastAction, instanceOf(HeadRequest.class));
    }
}