package server.actions;

import server.Action;
import server.ResourceHandler;
import server.messages.HeaderParameterExtractor;
import server.messages.HttpRequest;
import server.messages.HttpResponse;
import server.messages.Range;

import java.util.Arrays;

import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.PARTIAL_CONTENT;

public class PartialContent implements Action {
    private HeaderParameterExtractor headerParameterExtractor;
    private ResourceHandler resourceHandler;

    public PartialContent(ResourceHandler resourceHandler, HeaderParameterExtractor headerParameterExtractor) {
        this.resourceHandler = resourceHandler;
        this. headerParameterExtractor = headerParameterExtractor;
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return headerParameterExtractor.hasPartialContentRange(request.headerParameters());
    }

    @Override
    public HttpResponse process(HttpRequest request) {
        byte[] readResource = resourceHandler.read(request.getRequestUri());
        Range partialContentRange = getRangeFrom(request, readResource);

        return anHttpResponseBuilder()
                .withStatusCode(PARTIAL_CONTENT)
                .withContentRange(partialContentRange.getStartingIndex(), partialContentRange.getFinishingIndex())
                .withBody(extractPortionOfResource(readResource, partialContentRange.getStartingIndex(), partialContentRange.getFinishingIndex()))
                .build();
    }

    private Range getRangeFrom(HttpRequest request, byte[] readResource) {
        return headerParameterExtractor.getPartialContentRange(request.headerParameters(), readResource.length);
    }

    private byte[] extractPortionOfResource(byte[] readResource, int startingIndex, int finishingIndex) {
        return Arrays.copyOfRange(readResource, startingIndex, checkForEndOfResource(readResource.length, finishingIndex));
    }

    private int checkForEndOfResource(int length, int finishingIndex) {
        if (finishIndexIsNotBeyondLengthOfResource(length, finishingIndex)) {
            return finishingIndex + 1;
        }
        return length;
    }

    private boolean finishIndexIsNotBeyondLengthOfResource(int length, int finishingIndex) {
        return finishingIndex + 1 <= length;
    }
}
