package server.actions.etag;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EtagGenerator {

    private final String algorithm;

    public EtagGenerator(EtagGenerationAlgorithm algorithm) {
       this.algorithm = algorithm.getAlgorithm();
    }

    public String calculateEtag(byte[] content) {
        try {
            MessageDigest messageDigest = getMessageDigest();
            messageDigest.update(content, 0, content.length);
            byte[] digest = messageDigest.digest();
            return new HexBinaryAdapter().marshal(digest).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            throw new EtagGeneratorException(e);
        }
    }

    protected MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }
}
