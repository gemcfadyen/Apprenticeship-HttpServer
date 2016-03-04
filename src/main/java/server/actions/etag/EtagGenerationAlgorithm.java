package server.actions.etag;

public enum EtagGenerationAlgorithm {
    SHA_1("SHA-1");

    private String algorithm;

    EtagGenerationAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
