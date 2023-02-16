package de.haevn.debug;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.elements.ReadOnlyTextField;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.ThemeHandler;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class DebugWindow extends AbstractMessageWindow {
    private static final DebugWindow INSTANCE = new DebugWindow();
    private static boolean isShowing = false;
    private final Slider slider = new Slider(100, 200, 100);

    private DebugWindow() {
        super();
        super.initialize(createView(), 300, 400);
        super.setTitle("Debug Window");
        super.setStyle(StageStyle.UTILITY);
        super.setStyle(StageStyle.TRANSPARENT);
    }

    public static DoubleProperty getUpdate() {
        return INSTANCE.slider.valueProperty();
    }

    public static DebugWindow showDebugWindow() {
        if (isShowing) {
            return INSTANCE;
        }
        INSTANCE.show();
        isShowing = true;
        return INSTANCE;
    }

    private GridPane createView() {
        final GridPane gridPane = new GridPane();
        gridPane.setBackground(Background.fill(Color.valueOf("#1e1e1e")));
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
        gridPane.add(Creator.createButton("Reload Stylesheet", e -> ThemeHandler.getInstance().reload()), 0, 6, 2, 1);

        Label widthLabel = new Label("Width");
        gridPane.add(widthLabel, 0, 7);
        gridPane.add(slider, 1, 7);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> widthLabel.setText("Width " + newValue.intValue())));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        gridPane.getColumnConstraints().addAll(column1, column2);
        return gridPane;
    }
}
