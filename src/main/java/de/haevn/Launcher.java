package de.haevn;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.ui.views.MainView;
import de.haevn.utils.PropertyHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import static de.haevn.api.GitHubApi.STYLESHEET_KEY;

public class Launcher extends Application {
    private static final String CONFIG_FILE = "config";
    public static final String VERSION = PropertyHandler.getInstance(CONFIG_FILE).get("app.version");
    public static final String BUILD = PropertyHandler.getInstance(CONFIG_FILE).get("app.build");
    public static final String NAME = PropertyHandler.getInstance(CONFIG_FILE).get("app.name");
    private static Launcher instance;
    private Scene scene;

    public static void main(String[] args) {
        RaiderIOApi.getInstance().update();
        GitHubApi.getInstance().update();
        launch(args);
    }

    public static void reloadStylesheet() {
        if (null == instance || null == instance.scene) {
            return;
        }
        final String stylesheetUrl = PropertyHandler.getInstance("urls").get(STYLESHEET_KEY);
        instance.scene.getStylesheets().clear();
        instance.scene.getStylesheets().add(stylesheetUrl);
    }

    public static void openWebsite(String url) {
        if (null != instance && null != instance.getHostServices()) {
            instance.getHostServices().showDocument(url);
        }
    }

    @SuppressWarnings("all")
    @Override
    public void init() throws Exception {
        super.init();
        Launcher.instance = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView mainView = new MainView();
        scene = new Scene(mainView, 1024, 840);
        scene.setFill(Paint.valueOf("#1e1e1e"));
        reloadStylesheet();

        primaryStage.setTitle(NAME + " " + VERSION + " (" + BUILD + ")");
        primaryStage.setScene(scene);


        var debugWindow = de.haevn.debug.DebugWindow.showDebugWindow();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.xProperty().addListener((observable, oldValue, newValue) -> debugWindow.setX(newValue.doubleValue() + primaryStage.getWidth()));
        primaryStage.yProperty().addListener((observable, oldValue, newValue) -> debugWindow.setY(newValue.doubleValue()));
        primaryStage.show();
    }


}
