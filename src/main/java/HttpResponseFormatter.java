import java.io.UnsupportedEncodingException;
import java.util.List;

public class HttpResponseFormatter implements ResponseFormatter {
    @Override
    public byte[] format(HttpResponse response) {
        try {
            return createFormatted(response);
        } catch (UnsupportedEncodingException e) {
            throw new HttpResponseException("Error in creating HTTP Response", e);
        }
    }

    protected byte[] createFormatted(HttpResponse response) throws UnsupportedEncodingException {
        String formattedHeader = createHeader(response);
        byte[] formattedBody = createBody(response);
        byte[] formattedResponse = createWholeResponse(formattedHeader, formattedBody);
        System.out.println("formatted response is  " + formattedResponse + "\n--------------\n");
        return formattedResponse;
    }

    private byte[] createWholeResponse(String formattedHeader, byte[] formattedBody) throws UnsupportedEncodingException {
        if (formattedBody != null) {
            byte[] headerBytes = formattedHeader.getBytes("UTF-8");
            byte[] c = new byte[headerBytes.length + formattedBody.length];
            System.arraycopy(headerBytes, 0, c, 0, headerBytes.length);
            System.arraycopy(formattedBody, 0, c, headerBytes.length, formattedBody.length);
            return c;
//            byte[] combined = list.toArray(new byte[list.size()]);
//            return ArrayUtil.formattedHeader.getBytes("UTF-8") + formattedBody;
        } else {
            return formattedHeader.getBytes("UTF-8");
        }
    }

    private byte[] createBody(HttpResponse response) {
        return response.body();
    }

    private String createHeader(HttpResponse response) {
        String formattedHeader = formatStatusLine(response);
        if (hasAllowMethods(response)) {
            formattedHeader += addAllowLineToHeader(response);
        }

        if (hasLocation(response)) {
            formattedHeader += addLocationToHeader(response);
        }

        formattedHeader += endOfHeader();
        return formattedHeader;
    }

    private String addAllowLineToHeader(HttpResponse response) {
        return endOfLine() + commaDelimitAllowParameters(response);
    }

    private boolean hasLocation(HttpResponse response) {
        String location = response.location();
        return !(location == null || location.equals(""));
    }

    private String addLocationToHeader(HttpResponse response) {
        return endOfLine() + "Location: " + response.location();
    }

    private boolean hasAllowMethods(HttpResponse response) {
        return response.allowedMethods().size() > 0;
    }

    private String commaDelimitAllowParameters(HttpResponse response) {
        List<HttpMethods> allowMethods = response.allowedMethods();
        String allowedLine = "";
        HttpMethods firstMethod;
        if (allowMethods.size() > 0) {
            firstMethod = allowMethods.get(0);
            for (int i = 1; i < allowMethods.size(); i++) {
                allowedLine += "," + allowMethods.get(i);
            }
            return "Allow: " + firstMethod + allowedLine;
        }
        return allowedLine;
    }

    private String formatStatusLine(HttpResponse response) {
        return response.httpVersion() + space()
                + response.statusCode() + space()
                + response.reasonPhrase();
    }

    private String endOfHeader() {
        return endOfLine() + endOfLine();
    }

    private String endOfLine() {
        return "\r\n";
    }

    private String space() {
        return " ";
    }
}
