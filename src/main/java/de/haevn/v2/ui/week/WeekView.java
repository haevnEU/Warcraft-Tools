package de.haevn.v2.ui.week;

import de.haevn.jfx.html.H1;
import de.haevn.mvc.IView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

class WeekView extends GridPane implements IView {
    private final Label lbAffix = new Label();
    public WeekView() {
        initUI();
    }

    public void initUI() {
        getChildren().clear();
        add(new H1("WeekView"), 0, 0, 2, 1);
        add(new Label("Affix:"), 0, 1);
        add(lbAffix, 1, 1);
    }

    public void bindLbAffix(SimpleStringProperty text) {
        lbAffix.textProperty().bind(text);
    }

}
