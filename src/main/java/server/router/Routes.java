package server.router;

import server.Action;
import server.ResourceHandler;
import server.actions.*;
import server.actions.etag.EtagGenerator;
import server.messages.HeaderParameterExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static server.actions.etag.EtagGenerationAlgorithm.SHA_1;
import static server.router.HttpMethods.*;

public class Routes {
    private Map<HttpMethods, List<Action>> routes = new HashMap<>();

    public Routes(ResourceHandler resourceHandler, HeaderParameterExtractor headerParameterExtractor) {
        routes = configure(resourceHandler, headerParameterExtractor);
    }

    public Map<HttpMethods, List<Action>> routes() {
        return routes;
    }

    private Map<HttpMethods, List<Action>> configure(ResourceHandler resourceHandler, HeaderParameterExtractor headerParameterExtractor) {
        routes.put(GET, asList(
                new UnknownRoute(),
                new ListResourcesInPublicDirectory(resourceHandler),
                new Redirect(),
                new Authorisation(new ReadResource(resourceHandler), new HeaderParameterExtractor()),
                new IncludeParametersInBody(),
                new PartialContent(resourceHandler, headerParameterExtractor),
                new ReadResource(resourceHandler)));

        routes.put(POST, asList(
                new MethodNotAllowed(headerParameterExtractor),
                new WriteResource(resourceHandler)
        ));

        routes.put(PUT, asList(
                new MethodNotAllowed(headerParameterExtractor),
                new WriteResource(resourceHandler)));

        routes.put(DELETE, singletonList(new DeleteResource(resourceHandler)));
        routes.put(OPTIONS, singletonList(new ReadResource(resourceHandler)));
        routes.put(PATCH, singletonList(new PatchResource(resourceHandler, new EtagGenerator(SHA_1), headerParameterExtractor)));
        routes.put(HEAD, singletonList(new HeadRequest()));

        return routes;
    }
}
