package server.filesystem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import server.ResourceHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FileResourceHandlerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private String absolutePath;
    private String resourceName = "/myData";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws IOException {
        temporaryFolder.create();
        temporaryFolder.newFolder("publicRoot");
        absolutePath = temporaryFolder.getRoot().getAbsolutePath();
    }

    @Test
    public void createsNewResource() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        resourceHandler.write(resourceName, "My=Data");

        assertThat(getContentOfResource(), is("My=Data".getBytes()));
    }

    @Test
    public void updatesAnExistingResource() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        resourceHandler.write(resourceName, "originalData");
        resourceHandler.write(resourceName, "UpdatedData");

        assertThat(getContentOfResource(), is("UpdatedData".getBytes()));
    }

    @Test
    public void triesToRemovesAResourceThatDoesntExist() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        boolean deleted = resourceHandler.delete("Non-Existing-Resource");
        assertThat(deleted, is(false));
    }

    @Test
    public void removesExistingResource() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        resourceHandler.write(resourceName, "My=Data");

        boolean isDeleted = resourceHandler.delete(resourceName);
        assertThat(isDeleted, is(true));
    }

    @Test
    public void exceptionThrownWhenErrorInWritingToResource() {
        expectedException.expect(ResourceWriteException.class);
        expectedException.expectMessage("Exception in writing to file " + resourceName);
        expectedException.expectCause(instanceOf(IOException.class));

        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath) {
            protected void writeContentToFile(String filename, String content, boolean shouldAppend) throws IOException {
                throw new IOException("Throws exception for test");
            }
        };
        resourceHandler.write(resourceName, "anything");
    }

    @Test
    public void readsContentOfExistingResource() throws IOException {
        File resource = setupResourceWithContent("My=Data");
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        byte[] resourceContent = resourceHandler.read("/" + resource.getName());

        assertThat(resourceContent, is("My=Data".getBytes()));
    }

    @Test
    public void readsContentOfNonExistingResource() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        byte[] resourceContent = resourceHandler.read("non-existing-resource");

        assertThat(resourceContent.length, is(0));
    }

    @Test
    public void listsContentOfDirectory() throws IOException {
        temporaryFolder.newFile("file-one");
        temporaryFolder.newFile("file-two");
        temporaryFolder.newFile("file-three");
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);

        String[] filenames = resourceHandler.directoryContent();
        assertThat(filenames.length, is(4));
    }

    @Test
    public void appendToContentOfResource() throws IOException {
        File resource = setupResourceWithContent("My=Data");
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);

        resourceHandler.append("/" + resource.getName(), "\nhello=world\n");
        resourceHandler.append("/" + resource.getName(), "good=bye\n");

        assertThat(resourceHandler.read("/" + resource.getName()),
                is("My=Data\nhello=world\ngood=bye\n".getBytes()));
    }

    @Test
    public void exceptionThrownWhenErrorInAppendingToResource() {
        expectedException.expect(ResourceWriteException.class);
        expectedException.expectMessage("Exception in appending to file " + resourceName);
        expectedException.expectCause(instanceOf(IOException.class));

        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath) {
            protected void writeContentToFile(String filename, String content, boolean shouldAppend) throws IOException {
                throw new IOException("Throws exception for test");
            }
        };
        resourceHandler.append(resourceName, "anything");
    }

    @Test
    public void patchAResource() throws IOException {
        File resource = setupResourceWithContent("My=Data");
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);

        resourceHandler.patch("/" + resource.getName(), "New Content", 3);

        byte[] read = resourceHandler.read("/" + resource.getName());
        assertThat(read, is("NewData".getBytes()));
    }

    @Test
    public void exceptionThrownWhenErrorInPatchingToResource() {
        expectedException.expect(ResourceWriteException.class);
        expectedException.expectMessage("Exception in patching file " + resourceName);
        expectedException.expectCause(instanceOf(IOException.class));

        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath) {
            protected void updateResource(String filename, String content, int contentLength) throws IOException {
                throw new IOException("Throws exception for test");
            }
        };
        resourceHandler.patch(resourceName, "anything", 3);
    }
    private File setupResourceWithContent(String resourceContent) throws IOException {
        File resource = temporaryFolder.newFile("resource");
        Writer fileWriter = new FileWriter(resource.getPath());
        fileWriter.write(resourceContent);
        fileWriter.flush();
        fileWriter.close();

        return resource;
    }

    private byte[] getContentOfResource() {
        ResourceHandler resourceHandler = new FileResourceHandler(absolutePath);
        return resourceHandler.read(resourceName);
    }
}
