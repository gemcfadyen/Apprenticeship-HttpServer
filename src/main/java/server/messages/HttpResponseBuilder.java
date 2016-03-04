package server.messages;

import server.router.HttpMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HttpResponseBuilder {
    private StatusCode statusCode;
    private List<HttpMethods> allowedMethods = new ArrayList<>();
    private byte[] body;
    private String location;
    private boolean includeAuthorisation;
    private String contentRange;
    private String eTag;

    public static HttpResponseBuilder anHttpResponseBuilder() {
        return new HttpResponseBuilder();
    }

    public HttpResponse build() {
        return new HttpResponse(statusCode, location, allowedMethods, includeAuthorisation, contentRange, eTag, body);
    }

    public HttpResponseBuilder withStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponseBuilder withAllowMethods(HttpMethods... supportedMethods) {
        Collections.addAll(allowedMethods, supportedMethods);
        return this;
    }

    public HttpResponseBuilder withLocation(String redirectUrl) {
        this.location = redirectUrl;
        return this;
    }

    public HttpResponseBuilder withAuthorisationRequest() {
        this.includeAuthorisation = true;
        return this;
    }

    public HttpResponseBuilder withContentRange(int startingIndex, int finishingIndex) {
        this.contentRange = "bytes=" + startingIndex + "-" + finishingIndex;
        return this;
    }

    public HttpResponseBuilder withETag(String eTag) {
        this.eTag = eTag;
        return this;
    }

    public HttpResponseBuilder withBody(byte[] content) {
        this.body = content;
        return this;
    }
}
