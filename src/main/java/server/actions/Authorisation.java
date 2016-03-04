package server.actions;

import server.Action;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.UNAUTHORISED;
import static server.router.Resource.LOGS;

public class Authorisation implements Action {
    private HeaderParameterExtractor headerParameterExtractor;
    private ReadResource readResource;

    public Authorisation(ReadResource readResource, HeaderParameterExtractor headerParameterExtractor) {
        this.headerParameterExtractor = headerParameterExtractor;
        this.readResource = readResource;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return headerParameterExtractor.hasAuthenticationCredentials(request.headerParameters())
                || request.getRequestUri().equals(LOGS.getPath());
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        return isAuthorised(getCredentialsFromRequest(request)) ?
                readResource.process(request) :
                anHttpResponseBuilder().withStatusCode(UNAUTHORISED)
                        .withAuthorisationRequest()
                        .build();
    }

    private String getCredentialsFromRequest(HttpRequest request) {
        return headerParameterExtractor.getAuthenticationCredentials(request.headerParameters());
    }

    private boolean isAuthorised(String credentials) {
        return hasAuthorisationParam(credentials)
                && isCorrect(credentials);
    }

    private boolean hasAuthorisationParam(String authorisationParam) {
        return !(authorisationParam == null);
    }

    private boolean isCorrect(String credentials) {
        String[] encrypted = credentials.split(space());
        for (String encryptedParam : encrypted) {
            if (hasCredentials(encryptedParam)
                    && hasAuthenticated(encryptedParam)) {
                return true;
            }
        }

        return false;
    }

    private String space() {
        return " ";
    }

    private boolean hasCredentials(String encryptedParam) {
        return !encryptedParam.equals("Basic");
    }

    private boolean hasAuthenticated(String encryptedParam) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedParam.trim());
            return decode(decodedBytes);
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean decode(byte[] decodedBytes) throws UnsupportedEncodingException {
        return new String(decodedBytes, "UTF-8").equals("admin:hunter2");
    }
}