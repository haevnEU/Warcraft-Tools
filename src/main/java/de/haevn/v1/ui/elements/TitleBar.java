package de.haevn.v1.ui.elements;

import de.haevn.v1.ui.elements.html.H1;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TitleBar extends VBox {
    private final H1 header = new H1();
    private final List<Label> lbSubtext = new ArrayList<>();

    public TitleBar() {
        this("");
    }

    public TitleBar(String text) {
        this(text, "white");
    }

    public TitleBar(String text, String color) {

        getStyleClass().addAll("base_layer_background", "haevn-widgets-roundcorner");


        header.setText(text);
        header.setStyle("-fx-text-fill: " + color + ";-fx-font-size: 24;-fx-font-weight: bolder");
        getChildren().add(header);
    }

    public void setHeader(String text) {
        header.setText(text);
    }

    public void addSubtext(Label... labels) {

        for (Label label : labels) {
            if (lbSubtext.contains(label)) {
                continue;
            }

            lbSubtext.add(label);
            getChildren().add(label);
        }

    }
}
