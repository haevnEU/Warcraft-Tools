package de.haevn.v1.ui.elements.html;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class VDIV extends VBox {

    public VDIV() {
    }

    public VDIV(Node... elements) {
        setContent(elements);
    }

    public void setContent(Node... elements) {
        setId("haevn-widgets-div");
        getChildren().clear();
        getChildren().addAll(elements);
    }
}
