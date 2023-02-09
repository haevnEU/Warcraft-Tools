package de.haevn.ui.views;

import de.haevn.abstraction.IView;
import de.haevn.controller.CurrentWeekController;
import de.haevn.controller.PlayerSearchController;
import de.haevn.ui.widgets.ButtonBar;

import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;

import static de.haevn.ui.utils.Creator.createButton;

public class MainView extends BorderPane implements IView {

    private final CurrentWeekView currentWeekView = new CurrentWeekView();
    private final PlayerSearchView playerSearchView = new PlayerSearchView();
    private final PlayerSearchController lookupController = new PlayerSearchController();
    private final PremadeGroupFilterView premadeGroupFilterView = new PremadeGroupFilterView();

    private final SettingsView settingsView = new SettingsView();

    public MainView() {
        ButtonBar buttonBox = new ButtonBar(Orientation.VERTICAL);
        buttonBox.add(createButton("Current Week", e -> setCenter(currentWeekView)));
        buttonBox.add(createButton("Search player", e -> setCenter(playerSearchView)));
        buttonBox.add(createButton("Premade Group Filter", e -> setCenter(premadeGroupFilterView)));
        buttonBox.add(createButton("Settings", e -> setCenter(settingsView)));

        setLeft(buttonBox.getPane());

        lookupController.link(playerSearchView, null);
        new CurrentWeekController(currentWeekView);
    }
}
