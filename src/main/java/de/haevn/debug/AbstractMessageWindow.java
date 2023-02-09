package de.haevn.debug;

import de.haevn.api.GitHubApi;
import de.haevn.utils.PropertyHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public abstract class AbstractMessageWindow {
    private final Stage stage = new Stage();

    protected void initialize(Pane root) {
        String style = PropertyHandler.getInstance("urls").get(GitHubApi.STYLESHEET_KEY);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(style);
        scene.setFill(Paint.valueOf("#1e1e1e"));

        stage.setResizable(false);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
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
}
