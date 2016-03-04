package server;

public interface ResourceHandler {
    void write(String filename, String content);
    void append(String filename, String content);
    void patch(String filename, String content, int contentLength);
    boolean delete(String filename);
    byte[] read(String filename);
    String[] directoryContent();
}
