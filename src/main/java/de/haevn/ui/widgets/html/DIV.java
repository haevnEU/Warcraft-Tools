package de.haevn.ui.widgets.html;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class DIV extends Pane {

    public DIV() {
    }

    public DIV(Node... elements) {
        setContent(elements);
    }

    public void setContent(Node... elements) {
        setId("haevn-widgets-div");
        getChildren().clear();
        getChildren().addAll(elements);
    }
}
