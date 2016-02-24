class WriteResource implements Action {
    private final ResourceHandler resourceHandler;

    public WriteResource(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        System.out.println("PUT /FORM");
        resourceHandler.write(request.getRequestUri(), request.getBody());

        return HttpResponseBuilder.anHttpResponseBuilder()
                .withStatus(200)
                .withReasonPhrase("OK")
                .withBody(request.getBody().getBytes())
                .build();
    }
}