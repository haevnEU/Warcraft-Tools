package de.haevn.ui.elements;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;

public class ImageButton extends Label {
    private final ImageView view;

    public ImageButton() {
        this(null, null);
    }

    public ImageButton(Image img) {
        this(img, null);
    }

    public ImageButton(Image img, EventHandler<MouseEvent> e) {
        if (null != e) {
            setOnAction(e);
        }
        if (null != img) {
            view = new ImageView(img);
            setGraphic(view);
        } else {
            view = null;
            setText("No Image");
        }
    }

    public static ImageButton createDiscordButton() {
        final InputStream iconSource = ImageButton.class.getResourceAsStream("/discord-mark-blue.png");
        if (null != iconSource) {
            Image img = new Image(iconSource);
            ImageButton button = new ImageButton(img);
            button.view.setFitHeight(32);
            button.view.setFitWidth(40);
            return button;
        }
        return new ImageButton();
    }

    public static ImageButton createCrossButton() {
        final InputStream iconSource = ImageButton.class.getResourceAsStream("/delete.png");
        if (null != iconSource) {
            Image img = new Image(iconSource);
            ImageButton button = new ImageButton(img);
            button.view.setFitHeight(32);
            button.view.setFitWidth(30);
            return button;
        }
        return new ImageButton();
    }

    public static ImageButton createNewspaperButton() {
        final InputStream iconSource = ImageButton.class.getResourceAsStream("/newspaper-folded.png");
        if (null != iconSource) {
            Image img = new Image(iconSource);
            return new ImageButton(img);
        }
        return new ImageButton();
    }

    public static ImageButton createRecordingButton() {
        final InputStream iconSource = ImageButton.class.getResourceAsStream("/recording.png");
        if (null != iconSource) {
            Image img = new Image(iconSource);
            return new ImageButton(img);
        }
        return new ImageButton();
    }

    public static ImageButton createEditButton() {
        final InputStream iconSource = ImageButton.class.getResourceAsStream("/edit.png");
        if (null != iconSource) {
            Image img = new Image(iconSource);
            ImageButton button = new ImageButton(img);
            button.view.setFitHeight(32);
            button.view.setFitWidth(30);
            return button;
        }
        return new ImageButton();
    }

    public void setOnAction(EventHandler<MouseEvent> e) {
        setOnMouseClicked(e);
    }

}