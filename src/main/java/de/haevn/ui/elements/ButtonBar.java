package de.haevn.ui.elements;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ButtonBar {
    private final Pane root;

    public ButtonBar() {
        this(Orientation.VERTICAL);
    }

    public ButtonBar(Orientation orientation, Button... buttons) {
        root = orientation == Orientation.VERTICAL ? new VBox() : new HBox();
        if (orientation == Orientation.VERTICAL) {
            root.getStyleClass().addAll("base_padding", "buttonbar-vertical");
        } else {
            root.getStyleClass().addAll("base_padding", "buttonbar-horizontal");
        }
        add(buttons);
    }

    public void add(Button... buttons) {
        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            root.getChildren().add(button);
        }
    }

    public Pane getPane() {
        return root;
    }
}
