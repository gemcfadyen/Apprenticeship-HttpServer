package server;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CommandLineArgumentParserTest {

    private final CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser("homeDir");

    @Test
    public void portParameter() {
        String[] commandLineArguments = new String[]{"-p", "8080"};
        int port = commandLineArgumentParser.extractPort(commandLineArguments);

        assertThat(port, is(8080));
    }

    @Test
    public void defaultsPortTo5000IfInvalidValueProvided() {
        String[] commandLineArguments = new String[] {"-p", "banana"};
        int port = commandLineArgumentParser.extractPort(commandLineArguments);

        assertThat(port, is(5000));
    }

    @Test
    public void defaultHostIsSetTo5000() {
        String[] commandLineArguments = new String[]{};
        int port = commandLineArgumentParser.extractPort(commandLineArguments);

        assertThat(port, is(5000));
    }

    @Test
    public void publicDirectoryParameter() {
        String[] commandLineArguments = new String[]{"-d", "/some/path/here"};
        String publicDirectory = commandLineArgumentParser.extractPublicDirectory(commandLineArguments);

        assertThat(publicDirectory, is("/some/path/here"));
    }

    @Test
    public void publicDirectoryDefaultsWhenNotSpecified() {
        String[] commandLineArguments = new String[]{};
        String publicDirectory = commandLineArgumentParser.extractPublicDirectory(commandLineArguments);

        assertThat(publicDirectory, is("homeDir/vendor/cob_spec/public"));
    }
}
