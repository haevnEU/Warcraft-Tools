package de.haevn.ui.elements.html;

import javafx.scene.control.Label;

public class H1 extends Label {
    public H1() {
        this("");
    }

    public H1(String text) {
        this(text, "white");
    }

    public H1(String text, String color) {
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 24;-fx-font-weight: bolder");
    }
}
