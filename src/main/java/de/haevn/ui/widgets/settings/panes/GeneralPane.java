package de.haevn.ui.widgets.settings.panes;

import de.haevn.enumeration.RegionEnum;
import de.haevn.model.Settings;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.ThemeHandler;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GeneralPane extends GridPane {
    public GeneralPane() {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));

        add(new Label("Theme"), 0, 0);

        final ComboBox<String> cbTheme = new ComboBox<>(FXCollections.observableArrayList(ThemeHandler.getInstance().getThemes()));
        cbTheme.valueProperty().bindBidirectional(ThemeHandler.getInstance().getCurrentTheme());
        add(cbTheme, 1, 0);


        add(new Label("Region"), 0, 1);
        final ComboBox<RegionEnum> cbRegion = new ComboBox<>(FXCollections.observableArrayList(RegionEnum.values()));
        PropertyHandler.getInstance("config").getOptional("app.region").ifPresent(region -> cbRegion.getSelectionModel().select(RegionEnum.getRegionByName(region)));
        cbRegion.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                PropertyHandler.getInstance("config").set("app.region", newValue.regionCode);
                Settings.getInstance().regionPropertyProperty().set(newValue);
            }
        });
        add(cbRegion, 1, 1);

    }
}
