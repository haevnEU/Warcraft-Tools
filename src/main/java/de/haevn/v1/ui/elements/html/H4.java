package de.haevn.v1.ui.elements.html;

import javafx.scene.control.Label;

public class H4 extends Label {
    public H4() {
        this("");
    }

    public H4(String text) {
        this(text, "white");
    }

    public H4(String text, String color) {
        setId("haevn-widgets-h3");
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 16;-fx-font-weight: bolder");
    }
}
