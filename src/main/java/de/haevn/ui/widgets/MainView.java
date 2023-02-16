package de.haevn.ui.widgets;

import de.haevn.abstraction.IView;
import de.haevn.ui.elements.ButtonBar;
import de.haevn.ui.widgets.currentweek.CurrentWeekWidget;
import de.haevn.ui.widgets.dungeon.DungeonWidget;
import de.haevn.ui.widgets.pgf.PremadeGroupFilterWidget;
import de.haevn.ui.widgets.resources.ResourcesWidget;
import de.haevn.ui.widgets.search.PlayerSearchWidget;
import de.haevn.ui.widgets.settings.SettingsWidget;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;

import static de.haevn.ui.utils.Creator.createButton;

public class MainView extends BorderPane implements IView {

    private final CurrentWeekWidget currentWeekWidget = new CurrentWeekWidget();
    private final PlayerSearchWidget playerSearchView = new PlayerSearchWidget();
    private final PremadeGroupFilterWidget premadeGroupFilterView = new PremadeGroupFilterWidget();
    private final DungeonWidget dungeonWidget = new DungeonWidget();
    private final ResourcesWidget resourcesWidget = new ResourcesWidget();

    private final SettingsWidget settingsView = new SettingsWidget();

    public MainView() {
        ButtonBar buttonBox = new ButtonBar(Orientation.VERTICAL);
        buttonBox.add(createButton("Current Week", e -> setCenter(currentWeekWidget.getView())));
        buttonBox.add(createButton("Search player", e -> setCenter(playerSearchView.getView())));
        buttonBox.add(createButton("Dungeon Enemies", e -> setCenter(dungeonWidget.getView())));

        buttonBox.add(createButton("Premade Group Filter", e -> setCenter(premadeGroupFilterView.getView())));
        buttonBox.add(createButton("Resources", e -> setCenter(resourcesWidget.getView())));
        buttonBox.add(createButton("Settings", e -> setCenter(settingsView.getView())));

        setLeft(buttonBox.getPane());

    }
}
