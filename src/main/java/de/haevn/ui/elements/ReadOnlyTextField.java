package de.haevn.ui.elements;


import javafx.scene.control.TextField;

public class ReadOnlyTextField extends TextField {
    public ReadOnlyTextField() {
        this("");
    }

    public ReadOnlyTextField(String text) {
        super(text);
        setEditable(false);
    }
}
