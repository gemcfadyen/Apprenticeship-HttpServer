package server;

public enum HttpMethods {
    GET,
    HEAD,
    OPTIONS,
    POST,
    PUT,
    DELETE,
    PATCH;

    public static boolean isBogus(String method) {
        try {
            HttpMethods.valueOf(method);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
