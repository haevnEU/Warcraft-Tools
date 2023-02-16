package de.haevn.ui.elements;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class RefreshButton extends Label {
    public RefreshButton() {
        this(null);
    }
    public RefreshButton(EventHandler<MouseEvent> e) {
        if(null != e){
            setOnAction(e);
        }
        setText("⟳");
    }

    public void setOnAction(EventHandler<MouseEvent> e) {
        setOnMouseClicked(e);
    }
}