package de.haevn.ui.widgets.html;

import javafx.scene.control.Label;

public class ErrorLabel extends Label {
    public ErrorLabel() {
        this("");
    }

    public ErrorLabel(String text) {
        this(text, "red");
    }

    public ErrorLabel(String text, String color) {
        setText(text);
        setStyle("-fx-text-fill: " + color + ";-fx-font-weight: bolder");
    }
}
