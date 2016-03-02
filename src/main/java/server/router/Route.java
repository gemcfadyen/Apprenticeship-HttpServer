package server.router;

public enum Route {
    HOME("/"),
    FORM("/form"),
    METHOD_OPTIONS("/method_options"),
    REDIRECT("/redirect"),
    FOOBAR("/foobar"),
    IMAGE_JPEG("/image.jpeg"), //generalise
    IMAGE_PNG("/image.png"),
    IMAGE_GIF("/image.gif"),
    FILE("/file1"), //generalise
    TEXT_FILE("/text-file.txt"),
    PARAMETERS("/parameters"),
    LOGS("/logs"),
    LOG("/log"),
    THESE("/these"),
    REQUESTS("/requests"),
    PARTIAL_CONTENT("/partial_content.txt"),
    PATCH_CONTENT("/patch-content.txt");

    private final String path;

    Route(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static Route getRouteFor(String requestUri) {
        for (Route route : values()) {
            if (route.getPath().equals(requestUri)) {
                return route;
            }
        }
        throw new IllegalArgumentException("Unsupported route!");
    }

    public static boolean isSupported(String route) {
        for (Route value : Route.values()) {
            if (value.getPath().equals(route)) {
                return true;
            }
        }
        return false;
    }
}
