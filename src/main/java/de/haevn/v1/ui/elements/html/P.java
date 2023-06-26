package de.haevn.v1.ui.elements.html;

import javafx.scene.control.Label;

public class P extends Label {
    public P() {
        this("");
    }

    public P(String text) {
        this(text, "white");
    }

    public P(String text, String color) {
        setId("haevn-widgets-p");
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 16;");
    }
}
