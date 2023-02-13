package de.haevn.ui.widgets.html;

import de.haevn.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class AH3 extends Hyperlink {
    private String link;

    public AH3() {
        this("", "");
    }

    public AH3(String text, String link) {
        this(text, link, "white");
    }

    public AH3(String text, String link, String color) {
        setText(text);
        setLink(link);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 18;-fx-font-weight: bolder");
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
