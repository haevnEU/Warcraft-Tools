package de.haevn.ui.elements;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;

public class ImageButton extends Label {
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
        if(null != img) {
            ImageView view = new ImageView(img);
            view.setFitHeight(32);
            view.setFitWidth(40);
            setGraphic(view);
        }else{
            setText("No Image");
        }
    }

    public void setOnAction(EventHandler<MouseEvent> e) {
        setOnMouseClicked(e);
    }

    public static ImageButton createDiscordButton(){


        InputStream iconSource = ImageButton.class.getResourceAsStream("/discord-mark-blue.png");
        if(null != iconSource){
            Image img = new Image(iconSource);
            return new ImageButton(img);
        }
        return new ImageButton();
    }
}