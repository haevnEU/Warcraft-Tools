package de.haevn.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.haevn.enumeration.RatingEnum;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Optional;

public final class JsonAndStringUtils {
    private static final ObjectMapper jsonMapper = new JsonMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final ObjectMapper xmlMapper = new XmlMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonAndStringUtils() {
    }

    public static <T> T parseXml(String json, Class<T> type) throws JsonProcessingException {
        return xmlMapper.readValue(json, type);
    }

    public static <T> T parseXml(String json, TypeReference<T> type) throws JsonProcessingException {
        return xmlMapper.readValue(json, type);
    }


    public static <T> T parse(String json, Class<T> type) throws JsonProcessingException {
        return jsonMapper.readValue(json, type);
    }

    public static <T> T parse(String json, TypeReference<T> type) throws JsonProcessingException {
        return jsonMapper.readValue(json, type);
    }


    public static <T> Optional<T> parseSecure(String json, Class<T> type) {
        try {
            return Optional.of(jsonMapper.readValue(json, type));
        } catch (JsonProcessingException ex) {
            return Optional.empty();
        }
    }


    public static void copy(String text) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public static String getSuggestionIcon(RatingEnum rating) {
        return switch (rating) {
            case POSITIVE:
                yield "▲";
            case NEGATIVE:
                yield "▼";
            default:
                yield "▶";
        };
    }

    public static Optional<String> exportJson(Object json) {
        try {
            final String result = jsonMapper.writeValueAsString(json);
            return Optional.of(result);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
