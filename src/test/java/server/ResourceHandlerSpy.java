package server;

public class ResourceHandlerSpy implements ResourceHandler {
    private boolean hasWrittenFile;
    private boolean hasDeletedResource;
    private boolean hasReadResource;
    private boolean hasGotDirectoryContent;
    private boolean hasAppended;

    private String appendedFileName;
    private String contentAppendedToResource;
    private boolean hasPatchedResource;

    @Override
    public void write(String filename, String content) {
        hasWrittenFile = true;
    }

    @Override
    public void append(String filename, String content) {
        this.appendedFileName = filename;
        this.contentAppendedToResource = content;
        hasAppended = true;
    }

    @Override
    public void patch(String filename, String content, int contentLength) {
        hasPatchedResource = true;
    }

    @Override
    public boolean delete(String filename) {
        hasDeletedResource = true;
        return false;
    }

    @Override
    public byte[] read(String filename) {
        hasReadResource = true;
        return "My=Data".getBytes();
    }

    @Override
    public String[] directoryContent() {
        hasGotDirectoryContent = true;
        return new String[]{"file1", "file2"};
    }

    public boolean hasWrittenToResource() {
        return hasWrittenFile;
    }

    public boolean hasDeletedResource() {
        return hasDeletedResource;
    }

    public boolean hasReadResource() {
        return hasReadResource;
    }

    public boolean hasGotDirectoryContent() {
        return hasGotDirectoryContent;
    }

    public String getNameOfResourceThatWasAppended() {
        return appendedFileName;
    }

    public boolean hasAppendedToResource() {
        return hasAppended;
    }

    public String getContentAppendedToResource() {
        return contentAppendedToResource;
    }

    public boolean hasPatchedResource() {
        return hasPatchedResource;
    }
}
