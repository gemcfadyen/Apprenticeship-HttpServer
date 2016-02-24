public interface ResourceFinder {
    byte[] getContentOf(String resourcePath);
    byte[] getImageContent(String resourcePath);
}

