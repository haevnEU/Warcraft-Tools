package de.haevn.ui.views;

import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.enumeration.RegionEnum;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.RefreshButton;
import de.haevn.ui.widgets.html.A;
import de.haevn.ui.widgets.html.H1;
import de.haevn.ui.widgets.html.H3;
import de.haevn.utils.PropertyHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.util.Date;

public class SettingsView extends BorderPane implements IView {
    private final Label lbLastRaiderIOUpdate = new Label();
    private final Label lbLastGitHubUpdate = new Label();

    public SettingsView() {
        setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().add(Creator.generateTitledPane("Help", createHelp(), true));
        root.getChildren().add(Creator.generateTitledPane("Datasources", createDataSources(), true));
        root.getChildren().add(Creator.generateTitledPane("Updates", createRefresh(), true));
        scrollPane.setContent(root);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setTop(new H1("Settings"));
        setCenter(scrollPane);

        GitHubApi.getInstance().getLastUpdate().addListener((e, ignore, date) -> onGitHubApiUpdate(date));
        RaiderIOApi.getInstance().getLastUpdate().addListener((e, ignore, date) -> onRaiderIOUpdate(date));
        onGitHubApiUpdate(GitHubApi.getInstance().getLastUpdate().getValue());
        onRaiderIOUpdate(RaiderIOApi.getInstance().getLastUpdate().getValue());
    }

    private void onRaiderIOUpdate(Date date) {
        Platform.runLater(() -> {
            if (date == null || date.before(Date.from(Instant.EPOCH))) {
                lbLastRaiderIOUpdate.setText("failed");
            } else {
                lbLastRaiderIOUpdate.setText(date.toString());
            }
        });
    }

    private void onGitHubApiUpdate(Date date) {
        Platform.runLater(() -> {
            if (date == null || date.before(Date.from(Instant.EPOCH))) {
                lbLastGitHubUpdate.setText("failed");
            } else {
                lbLastGitHubUpdate.setText(date.toString());
            }
        });
    }

    private GridPane createHelp() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);


        pane.add(new H3("Support"), 0, 0, 2, 1);

        pane.add(new A("Repository", "https://github.com/nimile/WarcraftTools"), 0, 1, 2, 1);

        pane.add(new A("BugReport", "https://github.com/nimile/WarcraftTools/issues/new?template=bug_report.md"), 0, 2, 2, 1);
        pane.add(new A("Feature Request", "https://github.com/nimile/WarcraftTools/issues/new?template=feature_request.md"), 0, 3, 2, 1);


        pane.add(new H3("Author"), 0, 4);
        pane.add(new Label("Haevn"), 1, 4);

        pane.add(new Label("Discord"), 0, 5);
        pane.add(new A("Discord server", "https://discord.gg/NwpAb8Tcgk"), 1, 5);

        pane.add(new Label("Twitter"), 0, 6);
        pane.add(new A("@haevneu", "https://twitter.com/haevneu"), 1, 6);

        pane.add(new Label("Twitch"), 0, 7);
        pane.add(new A("haevneu", "https://www.twitch.tv/haevneu"), 1, 7);

        pane.add(new Label("Youtube"), 0, 8);
        pane.add(new A("@haevneu", "https://www.youtube.com/@haevneu"), 1, 8);

        return pane;
    }

    private VBox createDataSources() {
        VBox pane = new VBox();
        pane.setSpacing(10);
        pane.getChildren().add(new Label("The following datasource were used."));
        pane.getChildren().add(new A("Premade Group Filter Keywords", "https://github.com/0xbs/premade-groups-filter/wiki/Keywords"));
        pane.getChildren().add(new A("raider.io", "https://raider.io/"));
        return pane;
    }

    private GridPane createRefresh() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);

        pane.add(new Label("Region"), 0, 0);
        final ComboBox<RegionEnum> cbRegion = new ComboBox<>(FXCollections.observableArrayList(RegionEnum.EU, RegionEnum.US));

        cbRegion.valueProperty().addListener((observable, oldValue, newValue) -> {
            PropertyHandler.getInstance("config").set("app.region", newValue.toString());
            RaiderIOApi.getInstance().update();
        });
        cbRegion.getSelectionModel().select(RegionEnum.EU);
        pane.add(cbRegion, 1, 0);

        pane.add(new Label("RaiderIO"), 0, 1);
        pane.add(lbLastRaiderIOUpdate, 1, 1);
        pane.add(new RefreshButton(e -> RaiderIOApi.getInstance().update()), 2, 1);

        pane.add(new Label("GitHub"), 0, 2);
        pane.add(lbLastGitHubUpdate, 1, 2);
        pane.add(new RefreshButton(e -> GitHubApi.getInstance().update()), 2, 2);

        return pane;
    }
}
