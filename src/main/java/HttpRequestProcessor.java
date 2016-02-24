public class HttpRequestProcessor implements RequestProcessor {

    private ResourceFinder resourceFinder;
    private ResourceHandler resourceHandler;

    public HttpRequestProcessor(ResourceFinder resourceFinder, ResourceHandler resourceHandler) {
        this.resourceFinder = resourceFinder;
        this.resourceHandler = resourceHandler;
    }

    @Override
    public HttpResponse process(HttpRequest httpRequest) {
        System.out.println("Routing the request " + httpRequest.getRequestUri());
        HttpResponseBuilder httpResponseBuilder = HttpResponseBuilder.anHttpResponseBuilder();
        if (httpRequest.getRequestUri().equals("/")) {
            httpResponseBuilder.withStatus(200).withReasonPhrase("OK");
        } else if (httpRequest.getRequestUri().equals("/form")) {
            System.out.println("routing at /form with " + httpRequest.getMethod());
            if (httpRequest.getMethod().equals(HttpMethods.GET.name())) {
                System.out.println("GET /FORM");
                String body = resourceFinder.getContentOf(httpRequest.getRequestUri());
                System.out.println("BODY FROM THE GET IS " + body);
                httpResponseBuilder.withBody(body);
            }

            if (httpRequest.getMethod().equals(HttpMethods.POST.name())) {
                System.out.println("POST /FORM");
                resourceHandler.write(httpRequest.getRequestUri(), httpRequest.getBody());
                httpResponseBuilder.withBody(httpRequest.getBody());
            }

            if (httpRequest.getMethod().equals(HttpMethods.PUT.name())) {
                System.out.println("PUT /FORM");
                resourceHandler.write(httpRequest.getRequestUri(), httpRequest.getBody());
                httpResponseBuilder.withBody(httpRequest.getBody());
            }

            if (httpRequest.getMethod().equals(HttpMethods.DELETE.name())) {
                System.out.println("DELETE/FORM");
                resourceHandler.delete(httpRequest.getRequestUri());
            }
            httpResponseBuilder.withStatus(200).withReasonPhrase("OK");
        } else if (httpRequest.getRequestUri().equals("/method_options")) {
            httpResponseBuilder.withStatus(200).withReasonPhrase("OK").withAllowMethods(HttpMethods.values());
        } else if(httpRequest.getRequestUri().equals("/redirect")) {
            httpResponseBuilder.withStatus(302).withReasonPhrase("Found").withLocation("http://localhost:5000/");
        }

        else {
            httpResponseBuilder.withStatus(404).withReasonPhrase("Not Found");
        }

        return httpResponseBuilder.build();
    }
}