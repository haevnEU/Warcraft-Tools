package de.haevn.ui.elements.html;

import de.haevn.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class AH4 extends Hyperlink {
    private String link;

    public AH4() {
        this("", "");
    }

    public AH4(String text, String link) {
        this(text, link, "white");
    }

    public AH4(String text, String link, String color) {
        setText(text);
        setLink(link);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 16;-fx-font-weight: bolder");
        setOnAction(this::openLink);
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void openLink(ActionEvent event) {
        if (!link.isEmpty()) {
            Main.openWebsite(link);
        }
    }

}
