package server.messages;

import java.util.List;

public class DelimitedFormatter<T> {

    public String delimitedValues(List<T> values, Delimiter delimiter) {
        String delimiterSeparatedValues = "";
        T firstValue;
        if (values.size() > 0) {
            firstValue = values.get(0);
            for (int i = 1; i < values.size(); i++) {
                delimiterSeparatedValues += delimiter.get() + values.get(i);
            }
            return firstValue + delimiterSeparatedValues;
        }
        return delimiterSeparatedValues;
    }
}


