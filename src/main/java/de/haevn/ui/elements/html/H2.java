package de.haevn.ui.elements.html;

import javafx.scene.control.Label;

public class H2 extends Label {
    public H2() {
        this("");
    }

    public H2(String text) {
        this(text, "white");
    }

    public H2(String text, String color) {
        setId("haevn-widgets-h2");
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 20;-fx-font-weight: bolder");
    }
}
