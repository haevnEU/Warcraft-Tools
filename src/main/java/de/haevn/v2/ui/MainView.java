package de.haevn.v2.ui;

import de.haevn.jfx.UIElementsCreator;
import de.haevn.jfx.mvc.IJfxWidget;
import de.haevn.v2.ui.lookup.LookupWidget;
import de.haevn.v2.ui.week.WeekWidget;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
    private final IJfxWidget[] widgets = {
            new WeekWidget(),
            new LookupWidget()
    };

    public MainView() {
        getChildren().clear();
        final VBox box = new VBox();
        setLeft(box);
        box.setSpacing(10);
        setPadding(new Insets(10));
        for (IJfxWidget widget : widgets) {
            final Button btn = UIElementsCreator.createButton(widget.getName(), () -> setCenter(widget.getPane()));
            box.getChildren().add(btn);
        }



    }
}
