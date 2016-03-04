package server.actions;

import server.Action;
import server.ResourceHandler;
import server.messages.DelimitedFormatter;
import server.messages.HttpRequest;
import server.messages.HttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static server.messages.Delimiter.BREAK;
import static server.messages.HttpResponseBuilder.anHttpResponseBuilder;
import static server.messages.StatusCode.OK;
import static server.router.Resource.HOME;

public class ListResourcesInPublicDirectory implements Action {
    private ResourceHandler resourceHandler;
    private final DelimitedFormatter<String> listFormatter;

    public ListResourcesInPublicDirectory(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
        listFormatter = new DelimitedFormatter<>();
    }

    @Override
    public boolean isEligible(HttpRequest request) {
        return HOME.getPath().equals(request.getRequestUri());
    }

    public HttpResponse process(HttpRequest request) {
        return anHttpResponseBuilder()
                .withStatusCode(OK)
                .withBody(getDelimitedContentsOfDirectory().getBytes())
                .build();
    }

    private String getDelimitedContentsOfDirectory() {
        String[] filenames = resourceHandler.directoryContent();
        return listFormatter.delimitedValues(Arrays.asList(transformToLinks(filenames)), BREAK);
    }

    private String[] transformToLinks(String[] filenames) {
        return asLinks(filenames).toArray(new String[filenames.length]);
    }

    private List<String> asLinks(String[] filenames) {
        List<String> links = new ArrayList<>();
        for (String filename : filenames) {
            links.add(String.format("<a href=/%s>%s</a>", filename, filename));
        }
        return links;
    }
}
