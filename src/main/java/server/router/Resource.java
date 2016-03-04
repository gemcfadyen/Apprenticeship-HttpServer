package server.router;

public enum Resource {
    HOME("/"),
    FOOBAR("/foobar"),
    REDIRECT("/redirect"),
    LOGS("/logs");

    private final String path;

    Resource(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
