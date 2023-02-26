package de.haevn.ui.elements;

import de.haevn.ui.elements.html.H2;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ErrorWidget extends GridPane {

    private final Label lblMessage = new Label();

    public ErrorWidget() {
        this("");
    }

    public ErrorWidget(String message) {
        this("Something not quite right!", message, null);
    }

    public ErrorWidget(String title, String message, String imageUrl) {
        add(new H2(title), 0, 0);
        add(lblMessage, 0, 1);

        if (null != imageUrl && !imageUrl.isEmpty()) {
            final ImageView imageView = new ImageView(new Image(imageUrl));
            add(imageView, 1, 0, 1, 2);
        }
        setVgap(10);
        setHgap(10);
        setMessage(message);
    }

    public void setMessage(String message) {
        lblMessage.setText(message);
    }
}
