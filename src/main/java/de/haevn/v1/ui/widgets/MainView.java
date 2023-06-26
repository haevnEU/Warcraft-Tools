package de.haevn.v1.ui.widgets;

import de.haevn.v1.abstraction.IView;
import de.haevn.v1.ui.elements.ButtonBar;
import de.haevn.v1.ui.widgets.currentweek.CurrentWeekWidget;
import de.haevn.v1.ui.widgets.dungeon.DungeonWidget;
import de.haevn.v1.ui.widgets.pgf.PremadeGroupFilterWidget;
import de.haevn.v1.ui.widgets.recordvault.RecordVaultWidget;
import de.haevn.v1.ui.widgets.resources.ResourcesWidget;
import de.haevn.v1.ui.widgets.search.PlayerSearchWidget;
import de.haevn.v1.ui.widgets.settings.SettingsWidget;
import de.haevn.v1.utils.FileIO;
import de.haevn.v1.utils.SerializationUtils;
import de.haevn.v1.model.WarcraftResources;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

import static de.haevn.v1.ui.utils.Creator.createButton;

public class MainView extends BorderPane implements IView {

    private final CurrentWeekWidget currentWeekWidget = new CurrentWeekWidget();
    private final PlayerSearchWidget playerSearchView = new PlayerSearchWidget();
    private final PremadeGroupFilterWidget premadeGroupFilterView = new PremadeGroupFilterWidget();
    private final DungeonWidget dungeonWidget = new DungeonWidget();
    private final RecordVaultWidget recordArchiveWidget = new RecordVaultWidget();
    private final ResourcesWidget mythicPlusResourcesWidget;
    private final ResourcesWidget raidResourcesWidget;
    private final ResourcesWidget otherResourcesWidget;

    private final SettingsWidget settingsView = new SettingsWidget();

    public MainView() {
        final String json = FileIO.readFile("./bin/data/json/resources.json");
        Optional<WarcraftResources> resources = SerializationUtils.parseJsonSecure(json, WarcraftResources.class);
        mythicPlusResourcesWidget = resources.map(warcraftResources -> new ResourcesWidget(warcraftResources.getMythicplus())).orElse(null);
        raidResourcesWidget = resources.map(warcraftResources -> new ResourcesWidget(warcraftResources.getRaid())).orElse(null);
        otherResourcesWidget = resources.map(warcraftResources -> new ResourcesWidget(warcraftResources.getOther())).orElse(null);

        ButtonBar buttonBox = new ButtonBar(Orientation.VERTICAL);
        buttonBox.add(createButton("Current Week", e -> setCenter(currentWeekWidget.getView())));
        buttonBox.add(createButton("Search player", e -> setCenter(playerSearchView.getView())));
        buttonBox.add(createButton("Record Vault", e -> setCenter(recordArchiveWidget.getView())));
        buttonBox.add(createButton("Dungeon Enemies", e -> setCenter(dungeonWidget.getView())));

        buttonBox.add(createButton("Premade Group Filter", e -> setCenter(premadeGroupFilterView.getView())));

        if (null != mythicPlusResourcesWidget && mythicPlusResourcesWidget.isResourceAvailable()) {
            buttonBox.add(createButton("M+ Resources", e -> setCenter(mythicPlusResourcesWidget.getView())));
        }

        if (null != raidResourcesWidget && raidResourcesWidget.isResourceAvailable()) {
            buttonBox.add(createButton("Raid Resources", e -> setCenter(raidResourcesWidget.getView())));
        }
        if (null != otherResourcesWidget && otherResourcesWidget.isResourceAvailable()) {
            buttonBox.add(createButton("Other Resources", e -> setCenter(otherResourcesWidget.getView())));
        }


        buttonBox.add(createButton("Settings", e -> setCenter(settingsView.getView())));

        setLeft(buttonBox.getPane());

    }
}
