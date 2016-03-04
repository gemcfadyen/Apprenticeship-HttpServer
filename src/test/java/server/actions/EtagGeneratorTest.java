package server.actions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import server.actions.etag.EtagGenerator;
import server.actions.etag.EtagGeneratorException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.actions.etag.EtagGenerationAlgorithm.SHA_1;

public class EtagGeneratorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private final EtagGenerator etagGenerator = new EtagGenerator(SHA_1);

    @Test
    public void calculateEtagForDefaultContent() {
        String etag = etagGenerator.calculateEtag("default content".getBytes());
        assertThat(etag, is("dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec"));
    }

    @Test
    public void calculateEtagForPatchedContent() {
        String etag = etagGenerator.calculateEtag("patched content".getBytes());
        assertThat(etag, is("5c36acad75b78b82be6d9cbbd6143ab7e0cc04b0"));
    }

    @Test
    public void exceptionThrownIfEtagAlgorithmNotValid() {
        expectedException.expect(EtagGeneratorException.class);
        expectedException.expectMessage("Exception thrown whilst generating etag");
        expectedException.expectCause(instanceOf(NoSuchAlgorithmException.class));

        EtagGenerator etagGeneratorWhichThrowsAlgorithmException = new EtagGenerator(SHA_1) {
           protected MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
               throw new NoSuchAlgorithmException("Throws exception for test");
           }
        };
        etagGeneratorWhichThrowsAlgorithmException.calculateEtag("some-content".getBytes());
    }
}
