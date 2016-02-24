public class ResourceFinderSpy implements ResourceFinder {
    private boolean hasLookedUpResource;

    @Override
    public byte[] getContentOf(String resourcePath) {
        hasLookedUpResource = true;
        return "My=Data".getBytes();
    }

    @Override
    public byte[] getImageContent(String resourcePath) {
        return new byte[0];
    }

    public boolean hasLookedupResource() {
        return hasLookedUpResource;
    }
}
