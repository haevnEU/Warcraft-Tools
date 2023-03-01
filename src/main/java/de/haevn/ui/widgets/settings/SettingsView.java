package de.haevn.ui.widgets.settings;

import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.settings.panes.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Date;

class SettingsView extends BorderPane implements IView {


    private final WebhookPane webhookPane = new WebhookPane();
    private final GeneralPane generalPane = new GeneralPane();
    private final BackupPane backupPane = new BackupPane();
    private final HelpPane helpPane = new HelpPane();
    private final UpdatePane updatePane = new UpdatePane();
    private final DataSourcePane dataSourcePane = new DataSourcePane();
    private final ImageSource imageSource = new ImageSource();


    public SettingsView() {
        setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane();
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().add(Creator.generateTitledPane("Webhooks", webhookPane, true));
        root.getChildren().add(Creator.generateTitledPane("General", generalPane, true));
        root.getChildren().add(Creator.generateTitledPane("World of Warcraft Backup", backupPane, true));
        root.getChildren().add(Creator.generateTitledPane("Help", helpPane, false));
        root.getChildren().add(Creator.generateTitledPane("Updates", updatePane, false));
        root.getChildren().add(Creator.generateTitledPane("Datasources", dataSourcePane, false));
        root.getChildren().add(Creator.generateTitledPane("Image sources", imageSource, false));
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
        updatePane.onRaiderIOUpdate(date);
    }

    private void onGitHubApiUpdate(Date date) {
        updatePane.onGitHubApiUpdate(date);
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
