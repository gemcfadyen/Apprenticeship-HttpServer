package server.messages;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {

    private String requestLine;
    private String requestUri;
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> headerParameters = new HashMap<>();
    private String body;

    public static HttpRequestBuilder anHttpRequestBuilder() {
        return new HttpRequestBuilder();
    }

    public HttpRequestBuilder withRequestLine(String requestLine) {
        this.requestLine = requestLine;
        return this;
    }

    public HttpRequestBuilder withRequestUri(String uri) {
        this.requestUri = uri;
        return this;
    }

    public HttpRequestBuilder withHeaderParameters(Map<String, String> headerParams) {
        this.headerParameters = headerParams;
        return this;
    }

    public HttpRequestBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public HttpRequestBuilder withParameters(Map<String, String> requestParams) {
        this.requestParams = requestParams;
        return this;
    }

    public HttpRequest build() {
        return new HttpRequest(requestLine, requestUri, requestParams, headerParameters, body);
    }
}
