package de.haevn.utils;

import de.haevn.enumeration.RatingEnum;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public final class CustomStringUtils {
    private CustomStringUtils() {
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
}
