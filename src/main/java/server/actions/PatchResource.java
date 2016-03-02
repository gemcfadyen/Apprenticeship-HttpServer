package server.actions;

import server.Action;
import server.ResourceHandler;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.HttpResponseBuilder;

import java.util.Map;

import static server.messages.HttpMessageHeaderProperties.CONTENT_LENGTH;
import static server.messages.HttpMessageHeaderProperties.IF_MATCH;
import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.NO_CONTENT;
import static server.messages.StatusCode.PRECONDITION_FAILED;
import static server.router.Route.PATCH_CONTENT;

public class PatchResource implements Action {
    private ResourceHandler resourceHandler;
    private EtagGenerator etagGenerator;

    public PatchResource(ResourceHandler resourceHandler, EtagGenerator eTagGenerator) {
        this.resourceHandler = resourceHandler;
        this.etagGenerator = eTagGenerator;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return request.getRequestUri().equals(PATCH_CONTENT.getPath());
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        Map<String, String> headerParams = request.headerParameters();
        HttpResponseBuilder httpResponseBuilder = anHttpResponseBuilder();

        if (canUpdateResource(getResource(request.getRequestUri()), etagFromRequest(headerParams))) {
            patchResource(request, headerParams);
            httpResponseBuilder.withETag(etagFromRequest(headerParams));
            httpResponseBuilder.withStatusCode(NO_CONTENT);
        } else {
            httpResponseBuilder.withStatusCode(PRECONDITION_FAILED);
        }

        return httpResponseBuilder.build();
    }

    private byte[] getResource(String resourceName) {
        return resourceHandler.read(resourceName);
    }

    private void patchResource(HttpRequest request, Map<String, String> headerParams) {
        resourceHandler.patch(request.getRequestUri(), request.getBody(), contentLengthFromRequest(headerParams));
    }

    private Integer contentLengthFromRequest(Map<String, String> headerParams) {
        return Integer.valueOf(headerParams.get(CONTENT_LENGTH.getPropertyName()));
    }

    private String etagFromRequest(Map<String, String> headerParams) {
        return headerParams.get(IF_MATCH.getPropertyName());
    }

    private boolean canUpdateResource(byte[] originalContent, String requestEtag) {
        String generatedEtag = etagGenerator.calculateEtag(originalContent);
        return generatedEtag.equals(requestEtag);
    }
}
