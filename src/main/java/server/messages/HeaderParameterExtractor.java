package server.messages;

import java.util.Map;

import static server.messages.HttpMessageHeaderProperties.*;

public class HeaderParameterExtractor {

    public boolean hasAuthenticationCredentials(Map<String, String> headerParams) {
        return getAuthenticationCredentials(headerParams) != null;
    }

    public String getAuthenticationCredentials(Map<String, String> headerParams) {
        return headerParams.get(AUTHORISATION.getPropertyName());
    }

    public boolean hasPartialContentRange(Map<String, String> headerParams) {
        return headerParams.get(PARTIAL_CONTENT_RANGE.getPropertyName()) != null;
    }

    public Range getPartialContentRange(Map<String, String> headerParams, int resourceLength) {
        String contentRange = getRangeProperty(headerParams);
        String byteRange = getRangeReceived(contentRange);
        String[] providedStartAndFinishRange = splitStartAndFinishIndexesReceived(byteRange);

        if (rangeIsFullyProvided(providedStartAndFinishRange)) {
            return createRangeFrom(providedStartAndFinishRange);
        } else if (onlyFinishIndexProvided(providedStartAndFinishRange)) {
            return createRangeWithDerivedStartingIndex(resourceLength, providedStartAndFinishRange);
        } else {
            return createRangeWithDerivedFinishIndex(resourceLength, providedStartAndFinishRange);
        }
    }

    public boolean hasEtagProperty(Map<String, String> headerParams) {
        return headerParams.get(IF_MATCH.getPropertyName()) != null;
    }

    private Range createRangeWithDerivedFinishIndex(int resourceLength, String[] providedStartAndFinishRange) {
        String startingByte = providedStartAndFinishRange[0];
        return new Range(asNumeric(startingByte), resourceLength);
    }

    private Range createRangeWithDerivedStartingIndex(int resourceLength, String[] providedStartAndFinishRange) {
        int startingIndex = resourceLength - asNumeric(providedStartAndFinishRange[1]);
        return new Range(startingIndex, resourceLength);
    }

    private boolean onlyFinishIndexProvided(String[] providedStartAndFinishRange) {
        return providedStartAndFinishRange.length == 2;
    }

    private Range createRangeFrom(String[] startingByteAndFinishingByte) {
        String startingByte = startingByteAndFinishingByte[0];
        String finishingByte = startingByteAndFinishingByte[1];
        return new Range(asNumeric(startingByte), asNumeric(finishingByte));
    }

    private boolean rangeIsFullyProvided(String[] startingByteAndFinishingByte) {
        return startingByteAndFinishingByte.length == 2 && !startingByteAndFinishingByte[0].equals("");
    }

    private String[] splitStartAndFinishIndexesReceived(String byteRange) {
        return byteRange.split("-");
    }

    private String getRangeReceived(String contentRange) {
        String[] byteRange = contentRange.split("=");
        return byteRange[1];
    }

    private String getRangeProperty(Map<String, String> headerParams) {
        return headerParams.get(PARTIAL_CONTENT_RANGE.getPropertyName());
    }

    public boolean isReadOnly(Map<String, String> headerParameters) {
        String contentLengthFromRequest = headerParameters.get(CONTENT_LENGTH.getPropertyName());
        return contentLengthFromRequest == null || asNumeric(contentLengthFromRequest) <= 0;
    }

    private Integer asNumeric(String contentLengthFromRequest) {
        try {
            return Integer.valueOf(contentLengthFromRequest);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

