package de.haevn.ui.widgets.settings;

import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.enumeration.RegionEnum;
import de.haevn.logging.LoggerHandler;
import de.haevn.model.Settings;
import de.haevn.ui.elements.RefreshButton;
import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H3;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.settings.panes.BackupPane;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.ThemeHandler;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.util.Date;

class SettingsView extends BorderPane implements IView {
    private final Label lbLastRaiderIOUpdate = new Label();
    private final Label lbLastGitHubUpdate = new Label();
    private final TextField tfWebhookLogs = new TextField();

    private final BackupPane backupPane = new BackupPane();

    public SettingsView() {
        setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().add(Creator.generateTitledPane("Webhooks", createWebhooks(), true));
        root.getChildren().add(Creator.generateTitledPane("General", createGeneral(), true));
        root.getChildren().add(Creator.generateTitledPane("World of Warcraft Backup", backupPane, true));
        root.getChildren().add(Creator.generateTitledPane("Help", createHelp(), true));
        root.getChildren().add(Creator.generateTitledPane("Updates", createRefresh(), false));
        root.getChildren().add(Creator.generateTitledPane("Datasources", createDataSources(), false));
        root.getChildren().add(Creator.generateTitledPane("Image sources", createIconResource(), false));
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

    private GridPane createGeneral() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Theme"), 0, 0);

        final ComboBox<String> cbTheme = new ComboBox<>(FXCollections.observableArrayList(ThemeHandler.getInstance().getThemes()));
        cbTheme.valueProperty().bindBidirectional(ThemeHandler.getInstance().getCurrentTheme());
        grid.add(cbTheme, 1, 0);


        grid.add(new Label("Region"), 0, 1);
        final ComboBox<RegionEnum> cbRegion = new ComboBox<>(FXCollections.observableArrayList(RegionEnum.values()));
        PropertyHandler.getInstance("config").getOptional("app.region").ifPresent(region -> cbRegion.getSelectionModel().select(RegionEnum.getRegionByName(region)));
        cbRegion.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                PropertyHandler.getInstance("config").set("app.region", newValue.regionCode);
                Settings.getInstance().regionPropertyProperty().set(newValue);
            }
        });
        grid.add(cbRegion, 1, 1);

        return grid;
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
        final String repositoryUrl = PropertyHandler.getInstance("urls").get("settings.repo");
        final String issueUrl = PropertyHandler.getInstance("urls").get("settings.support.issue");
        final String featureUrl = PropertyHandler.getInstance("urls").get("settings.support.feature");

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
        pane.add(new A("Discord server", "https://discord.gg/pTnNVYv4Wd"), 1, 5);

        pane.add(new Label("Twitter"), 0, 6);
        pane.add(new A("@haevneu", "https://twitter.com/haevneu"), 1, 6);

        pane.add(new Label("Twitch"), 0, 7);
        pane.add(new A("haevneu", "https://www.twitch.tv/haevneu"), 1, 7);

        pane.add(new Label("Youtube"), 0, 8);
        pane.add(new A("@haevneu", "https://www.youtube.com/@haevneu"), 1, 8);

        return pane;
    }

    private GridPane createWebhooks() {
        final GridPane pane = new GridPane();
        GridPane.setHgrow(tfWebhookLogs, javafx.scene.layout.Priority.ALWAYS);
        pane.setHgap(10);
        pane.setVgap(5);
        pane.add(new Label("Webhook url"), 0, 0);
        pane.add(tfWebhookLogs, 1, 0);

        return pane;
    }

    private VBox createDataSources() {
        final String pgfRepositoryUrl = "https://github.com/0xbs/premade-groups-filter/wiki/Keywords";
        final String raiderIOUrl = "https://raider.io/";
        final VBox pane = new VBox();
        pane.setSpacing(10);
        pane.getChildren().add(new Label("The following datasource were used."));
        pane.getChildren().add(new A("Premade Group Filter Keywords", pgfRepositoryUrl));
        pane.getChildren().add(new A("raider.io", raiderIOUrl));
        return pane;
    }

    private VBox createIconResource() {
        final VBox pane = new VBox();
        pane.setSpacing(10);

        final String toolIcon = "https://www.flaticon.com/de/kostenlose-icons/werkzeuge";
        final String crossIcon = "https://www.flaticon.com/free-icons/trash";
        final String recordingIcon = "https://www.flaticon.com/free-icons/record";
        final String newsPaperIcon = "https://www.flaticon.com/free-icons/paper";
        final String editIcon = "https://www.flaticon.com/free-icons/edit";

        pane.getChildren().add(new A("Werkzeuge Icons erstellt von juicy_fish", toolIcon));
        pane.getChildren().add(new A("Trash icons created by Freepik", crossIcon));
        pane.getChildren().add(new A("Record icons created by Hilmy Abiyyu A.", recordingIcon));
        pane.getChildren().add(new A("Paper icons created by Freepik", newsPaperIcon));
        pane.getChildren().add(new A("Edit icons created by Kiranshastry", editIcon));
        return pane;
    }


    private GridPane createRefresh() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);

        pane.add(new Label("RaiderIO"), 0, 1);
        pane.add(lbLastRaiderIOUpdate, 1, 1);
        pane.add(new RefreshButton(e -> RaiderIOApi.getInstance().update()), 2, 1);

        pane.add(new Label("GitHub"), 0, 2);
        pane.add(lbLastGitHubUpdate, 1, 2);
        pane.add(new RefreshButton(e -> GitHubApi.getInstance().update()), 2, 2);

        pane.add(Creator.createButton("Flush logs", e -> LoggerHandler.flush()), 0, 3);
        return pane;
    }

    public void addOnWebhookLogAction(EventHandler<ActionEvent> event) {
        tfWebhookLogs.setOnAction(event);
    }

    public StringProperty getWebhookLogProperty() {
        return tfWebhookLogs.textProperty();
    }


    //----------------------------------------------------------------------------------------------------------------------
    // Backup interaction
    //----------------------------------------------------------------------------------------------------------------------

    public BooleanProperty getAutoBackupProperty() {
        return backupPane.getAutoBackup();
    }

    public StringProperty getBackupPathProperty() {
        return backupPane.getRootPath();
    }

    public void addOnBackupClicked(Runnable runnable) {
        backupPane.setOnButtonBackupClicked(runnable);
    }

    public void addOnRestoreClicked(Runnable runnable) {
        backupPane.setOnButtonRestoreClicked(runnable);
    }
}
