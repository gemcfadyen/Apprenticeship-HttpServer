import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileFinder implements ResourceFinder {
    private String rootPath;

    public FileFinder(String rootPath) {
        this.rootPath = rootPath;
    }

    public byte[] getImageContent(String resourcePath) {
        BufferedImage image = null;
//        try {
//            image = ImageIO.read(new File(rootPath + resourcePath));
//            System.out.println("IMAGE FOUND" + image);
//            return image;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            return Files.readAllBytes(Paths.get(filename(resourcePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        byte[] bytes;
//        try {
//            RandomAccessFile f = new RandomAccessFile(filename(resourcePath), "r");
//            bytes = new byte[(int) f.length()];
//            f.read(bytes);
//            f.close();
//            System.out.println("The image bytes are: " + bytes);
//            return bytes;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("no image found");
        return null;
    }

    public byte[] getContentOf(String resourcePath) {
        return getImageContent(resourcePath);
//        try {
//            System.out.println("Looking up resource at location: " + rootPath + resourcePath);
//            String content = readContentsOfFile(filename(resourcePath));
////            System.out.println("contents of file found " + content);
//            return content.getBytes("UTF-8");
//        } catch (IOException e) {
//            System.out.println("FILE IS NOT Found!!!");
//            return noResourceContentAvailable();
//        }
    }

    private String filename(String resourcePath) {
        return rootPath + resourcePath;
    }

    private String readContentsOfFile(String filename) throws IOException {
        BufferedReader resourceReader = new BufferedReader(new FileReader(filename));
        StringBuilder content = new StringBuilder("");

        String line = readLine(resourceReader);

        while (hasContent(line)) {
            content.append(line);
            line = readLine(resourceReader);
        }
        return content.toString();
    }

    private String readLine(BufferedReader resourceReader) throws IOException {
        return resourceReader.readLine();
    }

    private boolean hasContent(String line) {
        return line != null;
    }

    private byte[] noResourceContentAvailable() {
        return null;
    }
}
