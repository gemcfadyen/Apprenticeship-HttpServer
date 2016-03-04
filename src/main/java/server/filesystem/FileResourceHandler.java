package server.filesystem;

import server.ResourceHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileResourceHandler implements ResourceHandler {
    private String absolutePath;

    public FileResourceHandler(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    @Override
    public void write(String filename, String content) {
        try {
            writeContentToFile(filename, content, false);
        } catch (IOException e) {
            throw new ResourceWriteException("Exception in writing to file " + filename, e);
        }
    }

    @Override
    public void append(String filename, String content) {
        try {
            writeContentToFile(filename, content, true);
        } catch (IOException e) {
            throw new ResourceWriteException("Exception in appending to file " + filename, e);
        }
    }

    @Override
    public void patch(String filename, String content, int contentLength) {
        try {
            updateResource(filename, content, contentLength);
        } catch (IOException e) {
            throw new ResourceWriteException("Exception in patching file " + filename, e);
        }
    }

    @Override
    public boolean delete(String fileName) {
        File resource = new File(fullPath(fileName));
        return resource.delete();
    }

    @Override
    public byte[] read(String filename) {
        try {
            return Files.readAllBytes(Paths.get(fullPath(filename)));
        } catch (IOException e) {
            return noResourceContentAvailable();
        }
    }

    @Override
    public String[] directoryContent() {
        File rootDirectory = new File(absolutePath);
        return rootDirectory.list();
    }

    private byte[] noResourceContentAvailable() {
        return new byte[0];
    }

    private String fullPath(String fileName) {
        return absolutePath + fileName;
    }

    protected void writeContentToFile(String filename, String content, boolean shouldAppend) throws IOException {
        File resource = new File(fullPath(filename));
        FileWriter fileWriter = new FileWriter(resource, shouldAppend);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    protected void updateResource(String filename, String content, int contentLength) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fullPath(filename), "rw");
        randomAccessFile.write(content.getBytes(), 0, contentLength);
        randomAccessFile.close();
    }
}
