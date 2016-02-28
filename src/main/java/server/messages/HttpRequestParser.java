package server.messages;

import server.RequestParser;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static server.messages.HttpRequestBuilder.anHttpRequestBuilder;

public class HttpRequestParser implements RequestParser {
    private static final String QUESTION_MARK = "\\?";

    @Override
    public HttpRequest parse(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return parseRequest(reader);
    }

    protected HttpRequest parseRequest(BufferedReader reader) {
        try {
            return createHttpRequest(reader);
        } catch (IOException e) {
            throw new HttpRequestParsingException("Error in parsing Http Request", e);
        }
    }

    private HttpRequest createHttpRequest(BufferedReader reader) throws IOException {
        String[] requestLine = getRequestLine(reader);
        Map<String, String> headerParams = getHeaderParameters(reader);
        char[] bodyContents = getBody(reader, getContentLength(headerParams));

        return anHttpRequestBuilder()
                .withRequestLine(getMethod(requestLine))
                .withRequestUri(getRequestUri(requestLine))
                .withParameters(getRequestParams(requestLine))
                .withHeaderParameters(headerParams)
                .withBody(String.valueOf(bodyContents))
                .build();
    }

    private String[] getRequestLine(BufferedReader reader) throws IOException {
        String firstLine = readLine(reader);
        return splitUsing(space(), firstLine);
    }

    private String getRequestUri(String[] requestLine) {
        String firstLine = requestLine[1];
        String[] splitOnQuestionMark = splitUsing(QUESTION_MARK, firstLine);
        return splitOnQuestionMark[0];
    }

    private String getMethod(String[] requestLine) {
        return requestLine[0];
    }

    private Map<String, String> getHeaderParameters(BufferedReader reader) throws IOException {
        Map<String, String> headerParams = new HashMap<>();

        String line = readLine(reader);
        while (hasContent(line)) {
            String[] mapEntry = splitUsing(":", line);
            headerParams.put(headerParameterKey(mapEntry), headerParameterValue(mapEntry));
            line = readLine(reader);
        }

        return headerParams;
    }

    protected Map<String, String> getRequestParams(String[] methodLine) {
        Map<String, String> decodedParameters = new HashMap<>();
        String[] lineContainingAllParameters = parameterLine(methodLine[1]);
        for (int i = 1; i < lineContainingAllParameters.length; i++) {
            String[] parameterPair = splitUsing("&", lineContainingAllParameters[i]);

            for (String s : parameterPair) {
                String[] keyValueParameter = splitUsing("=", s);
                decodedParameters.put(keyValueParameter[0], decode(keyValueParameter[1]));
            }
        }
        return decodedParameters;
    }

    private String[] splitUsing(String delimiter, String line) {
        return line.split(delimiter);
    }

    private String[] parameterLine(String line) {
        return splitUsing(QUESTION_MARK, line);
    }

    private String decode(String parameter) {
        try {
            return decodeUsingUtf8(parameter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new HttpRequestParsingException("Error in parsing Http Request", e);
        }
    }

    protected String decodeUsingUtf8(String parameter) throws UnsupportedEncodingException {
        return URLDecoder.decode(parameter, "UTF-8");
    }

    private String headerParameterKey(String[] params) {
        return params[0].trim();
    }

    private String headerParameterValue(String[] params) {
        return params[1].trim();
    }

    private char[] getBody(BufferedReader reader, int contentLength) throws IOException {
        char[] bodyContents = new char[contentLength];
        reader.read(bodyContents, 0, contentLength);
        return bodyContents;
    }

    private String readLine(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    private boolean hasContent(String headerLine) {
        return !headerLine.isEmpty();
    }

    private int getContentLength(Map<String, String> headerParams) {
        String contentLengthFromHeader = headerParams.get("Content-Length");
        return contentLengthFromHeader != null ? Integer.valueOf(contentLengthFromHeader) : 0;
    }

    private String space() {
        return " ";
    }
}
