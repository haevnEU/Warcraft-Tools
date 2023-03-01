package de.haevn.ui.widgets.settings.panes;

import de.haevn.ui.utils.Creator;
import de.haevn.utils.AlertUtils;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class BackupPane extends GridPane {

    private final StringProperty backupPathProperty = new SimpleStringProperty();
    private final BooleanProperty enableAutoBackup = new SimpleBooleanProperty();

    private final Button btBackup = new Button("Create");
    private final Button btRestore = new Button("Restore");

    public BackupPane() {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));

        PropertyHandler.getInstance("config").getOptionalBoolean("backup.wow.auto").ifPresent(enableAutoBackup::set);
        PropertyHandler.getInstance("config").getOptional("backup.wow.path").ifPresent(backupPathProperty::set);


        final TextField tfWoWPath = new TextField();
        final HBox cbAutoBackup = Creator.createCheckBox("Create backup automatically", enableAutoBackup, true);
        final HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(btBackup, btRestore);

        add(new Label("Path to World of Warcraft.exe"), 0, 0);
        add(tfWoWPath, 1, 0);
        Button btChoose = new Button("Choose");
        add(btChoose, 2, 0);
        add(cbAutoBackup, 0, 1, 3, 1);
        add(buttonBox, 0, 2, 3, 1);

        tfWoWPath.setEditable(false);
        tfWoWPath.textProperty().bindBidirectional(backupPathProperty);
        tfWoWPath.setPromptText("Enter the path where the executable is located.");
        btChoose.setOnAction(e -> selectDirectory());
        enableAutoBackup.addListener((observable, old, newValue) -> PropertyHandler.getInstance("config").set("backup.wow.auto", Boolean.toString(newValue)));
        GridPane.setHgrow(tfWoWPath, Priority.ALWAYS);
    }

    private void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose");
        final var result = directoryChooser.showDialog(this.getScene().getWindow());
        if (null != result && new File(result, "Wow.exe").exists()) {
            backupPathProperty.set(result.getAbsolutePath());
            PropertyHandler.getInstance("config").set("backup.wow.path", backupPathProperty.get());
        } else {
            AlertUtils.showWarning("Invalid root path", "The selected directory does not contain the Wow.exe");
        }
    }

    public void setOnButtonBackupClicked(Runnable runnable) {
        btBackup.setOnAction(e -> runnable.run());
    }

    public void setOnButtonRestoreClicked(Runnable runnable) {
        btRestore.setOnAction(e -> runnable.run());
    }

    public StringProperty getRootPath() {
        return backupPathProperty;
    }

    public BooleanProperty getAutoBackup() {
        return enableAutoBackup;
    }
}
