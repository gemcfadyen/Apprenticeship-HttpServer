import java.util.List;


class HttpResponse {
    private final int statusCode;
    private String location;
    private final List<HttpMethods> allowedMethods;
    private String httpVersion;
    private String reasonPhrase;
    private byte[] body;

    protected HttpResponse(int statusCode,
                           String httpVersion,
                           String reasonPhrase,
                           String location,
                           List<HttpMethods> allowedMethods,
                           byte[] body) {
        this.statusCode = statusCode;
        this.httpVersion = httpVersion;
        this.location = location;
        this.reasonPhrase = reasonPhrase;
        this.location = location;
        this.allowedMethods = allowedMethods;
        this.body = body;
    }

    public int statusCode() {
        return statusCode;
    }

    public String httpVersion() {
        return httpVersion;
    }

    public String reasonPhrase() {
        return reasonPhrase;
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
}

