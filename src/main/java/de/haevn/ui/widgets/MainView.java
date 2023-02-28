package de.haevn.ui.widgets;

import de.haevn.abstraction.IView;
import de.haevn.model.WarcraftResources;
import de.haevn.ui.elements.ButtonBar;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.currentweek.CurrentWeekWidget;
import de.haevn.ui.widgets.dungeon.DungeonWidget;
import de.haevn.ui.widgets.pgf.PremadeGroupFilterWidget;
import de.haevn.ui.widgets.recordvault.RecordVaultWidget;
import de.haevn.ui.widgets.resources.ResourcesWidget;
import de.haevn.ui.widgets.search.PlayerSearchWidget;
import de.haevn.ui.widgets.settings.SettingsWidget;
import de.haevn.utils.FileIO;
import de.haevn.utils.SerializationUtils;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

import static de.haevn.ui.utils.Creator.createButton;

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
        final String json = FileIO.readFile("json/resources.json");
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

        final VBox resourceBox = new VBox();
        resourceBox.setSpacing(10);
        if (null != mythicPlusResourcesWidget && mythicPlusResourcesWidget.isResourceAvailable()) {
            resourceBox.getChildren().add(createButton("Mythic+", 130, e -> setCenter(mythicPlusResourcesWidget.getView())));
        }
        if (null != raidResourcesWidget && raidResourcesWidget.isResourceAvailable()) {
            resourceBox.getChildren().add(createButton("Raid", 130, e -> setCenter(raidResourcesWidget.getView())));
        }
        if (null != otherResourcesWidget && otherResourcesWidget.isResourceAvailable()) {
            resourceBox.getChildren().add(createButton("Other", 130, e -> setCenter(otherResourcesWidget.getView())));
        }

        buttonBox.add(Creator.generateTitledPane("Resources", resourceBox, false));

        buttonBox.add(createButton("Settings", e -> setCenter(settingsView.getView())));

        setLeft(buttonBox.getPane());
        setCenter(currentWeekWidget.getView());
    }
}
