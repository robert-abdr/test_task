package org.example.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {
    public LocalTimeDeserializer() {
        super(LocalTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        String timeStr = parser.getText();

        if (timeStr.matches("^\\d:\\d{2}$")) {
            timeStr = "0" + timeStr;
        }

        try {
            return LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            throw new IOException("Failed to parse time: " + timeStr, e);
        }
    }
}