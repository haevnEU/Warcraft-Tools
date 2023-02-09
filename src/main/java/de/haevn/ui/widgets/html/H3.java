package de.haevn.ui.widgets.html;

import javafx.scene.control.Label;

public class H3 extends Label {
    public H3() {
        this("");
    }

    public H3(String text) {
        this(text, "white");
    }

    public H3(String text, String color) {
        setId("haevn-widgets-h3");
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 18;-fx-font-weight: bolder");
    }
}
