package de.haevn.debug;

import de.haevn.Launcher;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.ReadOnlyTextField;
import de.haevn.utils.PropertyHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DebugWindow extends Stage {
    private static final DebugWindow INSTANCE = new DebugWindow();
    private static boolean isShowing = false;


    private DebugWindow() {
        super();
        setResizable(false);
        setTitle("Debug Tools");
        setScene(new Scene(createView(), 400, 600));
        initStyle(StageStyle.UTILITY);
    }

    private static GridPane createView() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Example Player"), 0, 0, 2, 1);
        gridPane.add(new ReadOnlyTextField("https://raider.io/characters/eu/eredar/Hævn?utm_source=addon"), 0, 1, 2, 1);
        gridPane.add(new ReadOnlyTextField("https://raider.io/characters/eu/eredar/Æsçãlåtïøñ?utm_source=addon"), 0, 2, 2, 1);
        gridPane.add(new ReadOnlyTextField("https://raider.io/characters/eu/"), 0, 3, 2, 1);

        gridPane.add(Creator.createButton("Reload properties", e -> PropertyHandler.reloadAll()), 0, 4, 2, 1);
        gridPane.add(Creator.createButton("Reload API", e -> {
            RaiderIOApi.getInstance().update();
            GitHubApi.getInstance().update();
        }), 0, 5, 2, 1);
        gridPane.add(Creator.createButton("Reload Stylesheet", e -> Launcher.reloadStylesheet()), 0, 6, 2, 1);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        gridPane.getColumnConstraints().addAll(column1, column2);
        return gridPane;
    }

    public static DebugWindow showDebugWindow() {
        if (isShowing) {
            return INSTANCE;
        }
        INSTANCE.show();
        isShowing = true;
        return INSTANCE;
    }
}
