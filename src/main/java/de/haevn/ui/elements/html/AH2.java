package de.haevn.ui.elements.html;

import de.haevn.utils.NetworkUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class AH2 extends Hyperlink {
    private String link;

    public AH2() {
        this("", "");
    }

    public AH2(String text, String link) {
        this(text, link, "white");
    }

    public AH2(String text, String link, String color) {
        setText(text);
        setLink(link);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 20;-fx-font-weight: bolder");
        setOnAction(this::openLink);
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void openLink(ActionEvent event) {
        if (!link.isEmpty()) {
            NetworkUtils.openWebsite(link);
        }
    }

}
