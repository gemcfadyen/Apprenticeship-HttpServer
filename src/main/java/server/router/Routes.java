package server.router;

import server.Action;
import server.HttpMethods;
import server.ResourceHandler;
import server.actions.*;
import server.messages.HeaderParameterExtractor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.HttpMethods.*;
import static server.actions.EtagGenerationAlgorithm.SHA_1;
import static server.messages.StatusCode.METHOD_NOT_ALLOWED;
import static server.messages.StatusCode.OK;

public class Routes {
    private Map<HttpMethods, List<Action>> routes = new HashMap<>();

    public Routes(ResourceHandler resourceHandler) {
        routes.put(GET, asList(
                new ListResourcesInPublicDirectory(resourceHandler),
                new Redirect(),
//                new IncludeParametersInBody(),
                new Authorisation(new ReadResource(resourceHandler), new HeaderParameterExtractor()),
//                new LogRequest(resourceHandler),
                new PartialContent(resourceHandler, new HeaderParameterExtractor()),
//                new PatchResource(resourceHandler, new EtagGenerator(SHA_1)),
//                new UnknownRoute(),
                new ReadResource(resourceHandler)));
        routes.put(POST, asList(new WriteResource(resourceHandler, OK)));
        routes.put(PUT, asList(
                new LogRequest(resourceHandler),
                new WriteResource(resourceHandler, METHOD_NOT_ALLOWED)));
        routes.put(DELETE, asList(new DeleteResource(resourceHandler)));
        routes.put(OPTIONS, asList(new MethodOptions()));
        routes.put(HEAD,
                asList(new LogRequest(resourceHandler),
                new MethodOptions()));
        routes.put(PATCH, asList(new PatchResource(resourceHandler, new EtagGenerator(SHA_1))));
//        routes.put(new RoutingCriteria(FORM, POST), new WriteResource(resourceHandler));
//        routes.put(new RoutingCriteria(FORM, PUT), new WriteResource(resourceHandler));
//        routes.put(new RoutingCriteria(FORM, DELETE), new DeleteResource(resourceHandler));
//        routes.put(new RoutingCriteria(METHOD_OPTIONS, OPTIONS), new MethodOptions());
//        routes.put(new RoutingCriteria(FILE, GET), new ReadResource(resourceHandler));
//        routes.put(new RoutingCriteria(FILE, PUT), new MethodNotAllowed());
//        routes.put(new RoutingCriteria(TEXT_FILE, POST), new MethodNotAllowed());
//        routes.put(new RoutingCriteria(THESE, PUT), new LogRequest(resourceHandler));
//        routes.put(new RoutingCriteria(REQUESTS, HEAD), new LogRequest(resourceHandler));
//        routes.put(new RoutingCriteria(PATCH_CONTENT, PATCH), new PatchResource(resourceHandler, new EtagGenerator(SHA_1)));
    }

    private List<Action> asList(Action... actions) {
        return Arrays.asList(actions);
    }

    public Map<HttpMethods, List<Action>> routes() {
        return routes;
    }
}
