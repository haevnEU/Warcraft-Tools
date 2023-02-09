package de.haevn.ui.widgets.html;

import javafx.scene.control.Label;

public class I extends Label {
    public I() {
        this("");
    }

    public I(String text) {
        this(text, "white");
    }

    public I(String text, String color) {
        setId("haevn-widgets-i");
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 16;-fx-font-weight: italic;");
    }
}
