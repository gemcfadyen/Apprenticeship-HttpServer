package server.messages;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private Map<String, String> requestParams = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();
    private final String requestUri;
    private final String body;

    protected HttpRequest(String method,
                          String requestUri,
                          Map<String, String> requestParams,
                          Map<String, String> headerParams,
                          String bodyContent) {
        this.method = method;
        this.requestUri = requestUri;
        this.requestParams = requestParams;
        this.headerParams = headerParams;
        this.body = bodyContent;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> headerParameters() {
        return headerParams;
    }

    public Map<String, String> params() {
        return requestParams;
    }

    public String getProtocolVersion() {
        return "HTTP/1.1";
    }
}


