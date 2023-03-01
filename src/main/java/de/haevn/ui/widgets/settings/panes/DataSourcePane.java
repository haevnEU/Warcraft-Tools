package de.haevn.ui.widgets.settings.panes;

import de.haevn.ui.elements.html.A;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSourcePane extends VBox {
    public DataSourcePane() {
        setSpacing(10);
        setPadding(new Insets(10));

        final String pgfRepositoryUrl = "https://github.com/0xbs/premade-groups-filter/wiki/Keywords";
        final String raiderIOUrl = "https://raider.io/";

        getChildren().add(new Label("The following datasource were used."));
        getChildren().add(new A("Premade Group Filter Keywords", pgfRepositoryUrl));
        getChildren().add(new A("raider.io", raiderIOUrl));
    }
}
