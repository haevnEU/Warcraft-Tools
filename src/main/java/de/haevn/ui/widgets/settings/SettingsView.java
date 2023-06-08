package de.haevn.ui.widgets.settings;

import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.logging.LoggerHandler;
import de.haevn.ui.elements.RefreshButton;
import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H3;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.PropertyKeys;
import de.haevn.utils.ThemeHandler;
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

class SettingsView extends BorderPane implements IView {
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
        final String repositoryUrl = PropertyHandler.getInstance("urls").get(PropertyKeys.SUPPORT_REPO);
        final String issueUrl = PropertyHandler.getInstance("urls").get(PropertyKeys.SUPPORT_ISSUE);
        final String featureUrl = PropertyHandler.getInstance("urls").get(PropertyKeys.SUPPORT_FEATURE);

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);


        pane.add(new H3("Support"), 0, 0, 2, 1);

        pane.add(new A("Repository", repositoryUrl), 0, 1, 2, 1);

        pane.add(new A("BugReport", issueUrl), 0, 2, 2, 1);
        pane.add(new A("Feature Request", featureUrl), 0, 3, 2, 1);


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
        final String pgfRepositoryUrl = "https://github.com/0xbs/premade-groups-filter/wiki/Keywords";
        final String raiderIOUrl = "https://raider.io/";
        final String iconUrl = "https://www.flaticon.com/de/kostenlose-icons/werkzeuge";
        final VBox pane = new VBox();
        pane.setSpacing(10);
        pane.getChildren().add(new Label("The following datasource were used."));
        pane.getChildren().add(new A("Premade Group Filter Keywords", pgfRepositoryUrl));
        pane.getChildren().add(new A("raider.io", raiderIOUrl));
        pane.getChildren().add(new A("Werkzeuge Icons erstellt von juicy_fish", iconUrl));
        return pane;
    }

    private GridPane createRefresh() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);

        pane.add(new Label("Style"), 0, 0);
        final ComboBox<String> cbRegion = new ComboBox<>(FXCollections.observableArrayList(ThemeHandler.getInstance().getThemes()));
        cbRegion.valueProperty().bindBidirectional(ThemeHandler.getInstance().getCurrentTheme());

        pane.add(cbRegion, 1, 0);

        pane.add(new Label("RaiderIO"), 0, 1);
        pane.add(lbLastRaiderIOUpdate, 1, 1);
        pane.add(new RefreshButton(e -> RaiderIOApi.getInstance().update()), 2, 1);

        pane.add(new Label("GitHub"), 0, 2);
        pane.add(lbLastGitHubUpdate, 1, 2);
        pane.add(new RefreshButton(e -> GitHubApi.getInstance().update()), 2, 2);

        pane.add(Creator.createButton("Flush logs", e -> LoggerHandler.flush()), 0, 3);
        return pane;
    }
}
