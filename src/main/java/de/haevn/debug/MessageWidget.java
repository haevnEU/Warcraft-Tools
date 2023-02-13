package de.haevn.debug;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

public class MessageWidget extends AbstractMessageWindow {
    public static final MessageWidget INSTANCE = new MessageWidget("");
    private final TextArea stacktrace = new TextArea();


    private MessageWidget(String text) {
        super();
        BorderPane root = new BorderPane();
        super.initialize(root);
        stacktrace.setEditable(false);
        stacktrace.setMaxHeight(Double.MAX_VALUE);
        stacktrace.setWrapText(true);
        stacktrace.setText(text);

        root.setCenter(stacktrace);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf("#1e1e1e"), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(10));
    }

    public static void show(String text) {
        INSTANCE.setText(text);
        INSTANCE.show();
    }

    public static void showAndWait(String text) {
        INSTANCE.setText(text);
        INSTANCE.showAndWait();
    }

    public void setText(String text) {
        stacktrace.setText(text);
    }

    public void appendText(String text) {
        stacktrace.appendText("\n" + text);
    }


}
