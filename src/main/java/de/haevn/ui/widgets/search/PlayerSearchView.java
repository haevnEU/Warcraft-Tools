package de.haevn.ui.widgets.search;

import de.haevn.abstraction.IView;
import de.haevn.api.DiscordApi;
import de.haevn.exceptions.NetworkException;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.elements.ErrorWidget;
import de.haevn.ui.elements.ProgressWidget;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H3;
import de.haevn.ui.elements.lookup.*;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.PropertyHandler;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class PlayerSearchView extends BorderPane implements IView {

    //----------------------------------------------------------------------------------------------------------------------
    //  Sometimes hidden elements
    //----------------------------------------------------------------------------------------------------------------------
    private final ErrorWidget errorWidget = new ErrorWidget();
    private final ProgressWidget progressWidget = new ProgressWidget();
    //----------------------------------------------------------------------------------------------------------------------
    //  Visible elements
    //----------------------------------------------------------------------------------------------------------------------
    private final SearchBox searchBox = new SearchBox();
    private final VBox centerBox = new VBox();
    //----------------------------------------------------------------------------------------------------------------------
    //  CenterBox content
    //----------------------------------------------------------------------------------------------------------------------
    private final AvatarWidget avatarWidget = new AvatarWidget();
    private final Label tfLastUpdate = new Label();
    private final OverviewView overviewView = new OverviewView();
    private final DungeonsView bestRuns = new DungeonsView("Best Runs");
    private final DungeonsView highestRuns = new DungeonsView("Highest Runs");
    private final DungeonsView recentRuns = new DungeonsView("Recent Runs");
    private final RaidView raidView = new RaidView("Raid");
    private final SimpleObjectProperty<PlayerLookupModel> player = new SimpleObjectProperty<>();


    public PlayerSearchView() {

        setTop(new VBox(new H1("Character Lookup"), searchBox));
        TabPane tabPane = new TabPane();
        tabPane.setPadding(new Insets(10));
        tabPane.getTabs().add(Creator.createTab("Overview", overviewView));
        tabPane.getTabs().add(Creator.createTab("Best Runs", bestRuns));
        tabPane.getTabs().add(Creator.createTab("Recent Runs", recentRuns));
        tabPane.getTabs().add(Creator.createTab("Highest Runs", highestRuns));
        tabPane.getTabs().add(Creator.createTab("Raid", raidView));

        centerBox.getChildren().addAll(avatarWidget, tfLastUpdate, tabPane);

        setCenter(new H3("Enter either a player name and realm or an raider.io url to start"));

        searchBox.addOnDiscordSendClicked(this::onButtonSendDiscordClicked);

        searchBox.setDiscordEnabled(!PropertyHandler.getInstance("config").get(DiscordApi.PLAYER_LOOKUP_KEY).isEmpty());
    }

    private void onButtonSendDiscordClicked() {
        if (null != player.get()) {
            DiscordApi.getInstance().sendPlayerWebhook(player.get());
        }
    }

    public ReadOnlyStringProperty getName() {
        return searchBox.getName();
    }

    public ReadOnlyStringProperty getRealm() {
        return searchBox.getRealm();
    }


    public void showPlayerDetails(PlayerLookupModel player) {
        Platform.runLater(() -> {
            setCenter(centerBox);
            avatarWidget.setPlayer(player);
            tfLastUpdate.setText(player.getLastCrawledAt().toString());
            bestRuns.setDungeons(player.getMythicPlusBestRuns());
            highestRuns.setDungeons(player.getMythicPlusHighestLevelRuns());
            recentRuns.setDungeons(player.getMythicPlusRecentRuns());
            overviewView.setPlayer(player);
            raidView.setLvRaids(player.getRaidProgression().toList());
            this.player.set(player);
        });
    }

    public void showError(Throwable ex) {
        Platform.runLater(() -> {
            if (ex instanceof NetworkException network) {
                if (NetworkUtils.is4xx(network.getStatusCode())) {
                    errorWidget.setMessage("Given Player " + searchBox.getNameString() + "-" + searchBox.getRealmString() + " in region " + PropertyHandler.getInstance("config").get("app.region") + " seems not existing.");
                } else if (NetworkUtils.is5xx(network.getStatusCode())) {
                    errorWidget.setMessage("It seems that the raider.io API is currently not available: " + network.getStatusCode());
                } else {
                    errorWidget.setMessage("An unknown error occurred, please rerun with debug mode enabled.");
                }
            }
            player.set(null);
            setCenter(errorWidget);
        });
    }

    public void showSearching() {
        Platform.runLater(() -> setCenter(progressWidget));
    }

    public void onButtonSearchClicked(EventHandler<ActionEvent> event) {
        searchBox.addOnSearchClicked(event);
    }

}
