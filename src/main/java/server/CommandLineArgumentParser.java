package server;

import java.util.Arrays;

public class CommandLineArgumentParser {
    private static final int DEFAULT_PORT = 5000;
    private static String DEFAULT_PUBLIC_DIRECTORY;

    public CommandLineArgumentParser(String homeDirectory) {
        DEFAULT_PUBLIC_DIRECTORY = homeDirectory + "/vendor/cob_spec/public";
    }

    public int extractPort(String... commandLineArgs) {
        int indexOfPortFlag = Arrays.asList(commandLineArgs).indexOf("-p");
        if (parameterKeyIsfound(indexOfPortFlag)) {
            String portAsString = extractParameterValue(commandLineArgs, indexOfPortFlag);
            return translatePortToNumericValue(portAsString);
        } else {
            return DEFAULT_PORT;
        }
    }

    public String extractPublicDirectory(String[] commandLineArgs) {
        int indexOfPublicDirectoryFlag = Arrays.asList(commandLineArgs).indexOf("-d");
        if (parameterKeyIsfound(indexOfPublicDirectoryFlag)) {
            return extractParameterValue(commandLineArgs, indexOfPublicDirectoryFlag);
        } else {
            return DEFAULT_PUBLIC_DIRECTORY;
        }
    }

    private boolean parameterKeyIsfound(int indexOfFlag) {
        return indexOfFlag != -1;
    }

    private String extractParameterValue(String[] args, int indexOfFlag) {
        return Arrays.asList(args).get(indexOfFlag + 1);
    }

    private int translatePortToNumericValue(String portAsString) {
        try {
            return convertToInteger(portAsString);
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }

    private Integer convertToInteger(String portAsString) {
        return Integer.valueOf(portAsString);
    }
}
