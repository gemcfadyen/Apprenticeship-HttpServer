package server.messages;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static server.messages.Delimiter.COMMA;

public class DelimitedFormatterTest {

    private DelimitedFormatter<String> delimitedFormatter = new DelimitedFormatter<>();

    @Test
    public void commaDelimits() {
        List<String> animals = new ArrayList<>();
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Monkey");

        String delimitedValues = delimitedFormatter.delimitedValues(animals, COMMA);

        assertThat(delimitedValues, is("Cat,Dog,Monkey"));
    }

    @Test
    public void noDelimiterWhenOnlyOneItemInList() {
        List<String> animals = new ArrayList<>();
        animals.add("Mouse");

        String delimitedValues = delimitedFormatter.delimitedValues(animals, COMMA);

        assertThat(delimitedValues, is("Mouse"));
    }

    @Test
    public void emptyStringReturnedWhenListIsEmpty() {
        String delimitedValues = delimitedFormatter.delimitedValues(new ArrayList<>(), COMMA);

        assertThat(delimitedValues, is(""));
    }
}