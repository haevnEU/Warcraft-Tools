package de.haevn;

import de.haevn.v1.api.GitHubApi;
import de.haevn.v1.api.RaiderIOApi;
import de.haevn.v1.logging.Logger;
import de.haevn.v1.logging.LoggerHandler;
import de.haevn.v1.ui.widgets.MainView;
import de.haevn.v1.utils.FileIO;
import de.haevn.v1.utils.PropertyHandler;
import de.haevn.v1.utils.PropertyKeys;
import de.haevn.v1.utils.ThemeHandler;
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
    private static final String CONFIG_FILE = "config";
    public static final String VERSION = PropertyHandler.getInstance(CONFIG_FILE).get(PropertyKeys.APP_VERSION);
    public static final String BUILD = PropertyHandler.getInstance(CONFIG_FILE).get(PropertyKeys.APP_BUILD);
    public static final String NAME = PropertyHandler.getInstance(CONFIG_FILE).get(PropertyKeys.APP_NAME);
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

        primaryStage.setTitle(NAME + " " + VERSION + " (" + BUILD + ")");

        Main.instance = this;


        final double width = 1024;
        final double height = 840;
        if(System.getenv().containsKey("PREVIEW")){
            scene = new Scene(new de.haevn.v2.ui.MainView(), width, height);
        }else{
            scene = new Scene( new MainView(), width, height);

        }
        scene.setFill(Paint.valueOf("#1e1e1e"));
        LOGGER.atInfo("Loading %s %s (%s)", NAME, VERSION, BUILD);
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
        if(null != iconSource){
            primaryStage.getIcons().add(new Image(iconSource));
        }

        LOGGER.atInfo("With dimensions: %s x %s", width, height);
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
