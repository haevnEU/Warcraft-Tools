package de.haevn;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.ui.widgets.MainView;
import de.haevn.utils.AppData;
import de.haevn.utils.FileIO;
import de.haevn.utils.ThemeHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {
    private static final Logger LOGGER = LoggerHandler.get(Main.class);
    public static final String VERSION = AppData.getInstance().getVersion();
    public static final String BUILD = AppData.getInstance().getBuild();
    public static final String NAME = AppData.getInstance().getName();
    private static Main instance;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }


    public static void loadStylesheet(URL url) {
        if (null == instance || null == instance.scene) {
            return;
        }
        instance.scene.getStylesheets().clear();
        instance.scene.getStylesheets().add(url.toExternalForm());
    }

    @Override
    public void start(Stage primaryStage) {
        var result = FileIO.validate();

        if (!result.getFirst()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Self check failed");
            alert.setContentText(result.getSecond() + "Please run the resource updater.");
            alert.showAndWait();

            System.exit(-1);
        }

        primaryStage.setTitle(NAME + " " + VERSION + " (" + BUILD + ")");
        InputStream iconSource = getClass().getResourceAsStream("/werkzeugkasten.png");
        if (null != iconSource) {
            primaryStage.getIcons().add(new Image(iconSource));
        }
        Main.instance = this;
        MainView mainView = new MainView();

        final double width = 1024;
        final double height = 840;
        LOGGER.atInfo("With dimensions: %s x %s", width, height);
        scene = new Scene(mainView, width, height);
        scene.setFill(Paint.valueOf("#1e1e1e"));

        primaryStage.setScene(scene);

        primaryStage.show();
        setup();
    }

    private void setup() {
        LOGGER.atInfo("Refreshing data");
        RaiderIOApi.getInstance().update();
        GitHubApi.getInstance().update();
        ThemeHandler.getInstance().reload();
        LOGGER.atInfo("Setup complete");
    }

}
