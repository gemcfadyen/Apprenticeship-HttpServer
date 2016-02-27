package server.messages;

public enum HttpMessageHeaderProperties {
    PARTIAL_CONTENT_RANGE("Range"),
    AUTHORISATION("Authorization"),
    AUTHENTICATE("WWW-Authenticate"),
    ALLOW("Allow"),
    LOCATION("Location");

    private String property;

    HttpMessageHeaderProperties(String property) {
        this.property = property;
    }

    public String getPropertyName() {
        return this.property;
    }
}
