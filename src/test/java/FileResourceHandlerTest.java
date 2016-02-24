import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

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
            protected void writeContentToFile(String filename, String content) throws IOException {
                throw new IOException("Throws exception for test");
            }
        };
        resourceHandler.write(resourceName, "anything");
    }

    private byte[] getContentOfResource() {
        ResourceFinder reader = new FileFinder(absolutePath);
        return reader.getContentOf(resourceName);
    }
}
