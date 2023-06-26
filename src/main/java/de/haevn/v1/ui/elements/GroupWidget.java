package de.haevn.v1.ui.elements;

import de.haevn.v1.ui.elements.html.H1;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GroupWidget extends VBox {
    private final ScrollPane root = new ScrollPane();

    public GroupWidget() {
        this("");
    }

    public GroupWidget(String text) {
        this(text, new Pane());
    }

    public GroupWidget(String text, Pane content) {
        this(text, content, new H1());
    }

    public GroupWidget(String text, Node content, Label header) {
        getStyleClass().addAll("base_layer_background", "haevn-widgets-roundcorner");
        header.setText(text);

        getChildren().addAll(header, root);

        root.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setContent(content);
    }

    public void setContent(Node content) {
        root.setContent(content);
    }
}
