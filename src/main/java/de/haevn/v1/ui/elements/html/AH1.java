package de.haevn.v1.ui.elements.html;

import de.haevn.v1.utils.NetworkUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class AH1 extends Hyperlink {
    private String link;

    public AH1() {
        this("", "");
    }

    public AH1(String text, String link) {
        this(text, link, "white");
    }

    public AH1(String text, String link, String color) {
        setText(text);
        setLink(link);
        setStyle("-fx-text-fill: " + color + ";-fx-font-size: 24;-fx-font-weight: bolder");
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
