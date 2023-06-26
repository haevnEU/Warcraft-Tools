package de.haevn.v1.ui.elements.html;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class HDIV extends HBox {

    public HDIV() {
    }

    public HDIV(Node... elements) {
        setContent(elements);
    }

    public void setContent(Node... elements) {
        setId("haevn-widgets-div");
        getChildren().clear();
        getChildren().addAll(elements);
    }
}
