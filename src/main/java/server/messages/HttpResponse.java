package server.messages;

import server.router.HttpMethods;

import java.util.List;


public class HttpResponse {
    private final StatusCode statusCode;
    private String location;
    private final List<HttpMethods> allowedMethods;
    private boolean authorisationRequest;
    private byte[] body;
    private String contentRange;
    private String eTag;

    protected HttpResponse(StatusCode statusCode,
                           String location,
                           List<HttpMethods> allowedMethods,
                           boolean authorisationRequest,
                           String contentRange,
                           String eTag,
                           byte[] body) {
        this.statusCode = statusCode;
        this.location = location;
        this.allowedMethods = allowedMethods;
        this.authorisationRequest = authorisationRequest;
        this.contentRange = contentRange;
        this.eTag = eTag;
        this.body = body;
    }

    public StatusCode statusCode() {
        return statusCode;
    }

    public String httpVersion() {
        return "HTTP/1.1";
    }

    public List<HttpMethods> allowedMethods() {
        return allowedMethods;
    }

    public byte[] body() {
        return body;
    }

    public String location() {
        return location;
    }

    public boolean authorisationRequest() {
        return authorisationRequest;
    }

    public String contentRange() {
        return contentRange;
    }

    public String eTag() {
        return eTag;
    }
}

