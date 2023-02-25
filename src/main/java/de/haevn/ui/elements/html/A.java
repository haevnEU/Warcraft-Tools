package de.haevn.ui.elements.html;

import de.haevn.Main;
import de.haevn.utils.NetworkUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

public class A extends Hyperlink {
    private String link;

    public A() {
        this("", "");
    }

    public A(String text, String link) {
        this(text, link, "white");
    }

    public A(String text, String link, String color) {
        setText(text);
        setLink(link);
        setStyle("-fx-text-fill: " + color);
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
