package de.haevn.debug;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractMessageWindow {
    private final Stage stage = new Stage();

    protected void initialize(Pane root) {
        initialize(root, 800, 600);
    }

    protected void initialize(Pane root, double width, double height) {

        Scene scene = new Scene(root, width, height);
        stage.setResizable(false);
        stage.setScene(scene);
    }


    public void show() {
        stage.show();
    }

    public void showAndWait() {
        stage.showAndWait();
    }



    public void hide() {
        stage.hide();
    }

    public void toggle() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    public void close() {
        stage.close();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public void setOnCloseRequest(Runnable runnable) {
        stage.setOnCloseRequest(event -> runnable.run());
    }


    public void setAlwaysOnTop(boolean b) {
        stage.setAlwaysOnTop(b);
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setStyle(StageStyle style) {
        stage.initStyle(style);
    }

    public void setX(double v) {
        stage.setX(v);
    }

    public void setY(double v) {
        stage.setY(v);
    }
}
