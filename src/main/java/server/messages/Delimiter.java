package server.messages;

public enum Delimiter {
    COMMA(","),
    COLON(":"),
    AMPERSAND("&"),
    EQUALS("="),
    QUESTION_MARK("\\?"),
    BREAK("<br>");

    private String delimiter;

    Delimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String get() {
        return delimiter;
    }

}
