package de.haevn.ui.widgets.recordvault;

import de.haevn.api.DiscordApi;
import de.haevn.model.recording.RecordEntry;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.AlertUtils;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class NewRecordWidget extends Stage {
    private static final NewRecordWidget instance = new NewRecordWidget();

    private final TextField textFieldName = new TextField();
    private final DatePicker textFieldRecordDate = new DatePicker();
    private final TextField textFieldWarcraftLogsLink = new TextField();
    private final TextField textFieldRecordingLocation = new TextField();
    private final TextArea textAreaTags = new TextArea();
    private final RadioButton rbRaid = new RadioButton("Raid");
    private final RadioButton rbMythicplus = new RadioButton("mythic+");
    private final CheckBox cbSendLog = new CheckBox("Warcraft Logs link");
    private final CheckBox cbSendRecording = new CheckBox("Recording link");
    private final ToggleGroup modeGroup = new ToggleGroup();
    private boolean dialogResult = false;

    private NewRecordWidget() {
        setTitle("Add new record");
        final GridPane root = new GridPane();
        final HBox buttonBox = new HBox();

        final H1 title = new H1("New recording");
        final Label lbName = new Label("Name");
        final Label lbTags = new Label("Tags");
        final Label lbDate = new Label("Date");
        final Label lbWarcraftLogsLink = new Label("Warcraft Logs link");
        final Label lbRecordingLocation = new Label("Recorded file");
        final Button btSelectFile = Creator.createButton("Select file", e -> selectRecordingFile());
        final Button btSave = Creator.createButton("Save", e -> save());
        final Button btCancel = Creator.createButton("Cancel", e -> cancel());

        modeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (modeGroup.getSelectedToggle() != null) {
                String tags;
                final RadioButton selected = (RadioButton) modeGroup.getSelectedToggle();
                if (selected.getText().equals("Raid")) {
                    tags = textAreaTags.getText().replace("\nmythic+", "");
                    tags += "\nraid";
                } else {
                    tags = textAreaTags.getText().replace("\nraid", "");
                    textAreaTags.setText(tags);
                    tags += "\nmythic+";
                }
                textAreaTags.setText(tags);
            }
        });
        rbRaid.setToggleGroup(modeGroup);
        rbMythicplus.setToggleGroup(modeGroup);
        rbRaid.setSelected(true);

        root.add(title, 0, 0, 3, 1);

        root.add(lbName, 0, 1);
        root.add(textFieldName, 1, 1, 2, 1);

        root.add(lbDate, 0, 2);
        root.add(textFieldRecordDate, 1, 2, 2, 1);

        root.add(lbWarcraftLogsLink, 0, 3);
        root.add(textFieldWarcraftLogsLink, 1, 3, 2, 1);

        root.add(lbRecordingLocation, 0, 4);
        root.add(btSelectFile, 1, 4);
        root.add(textFieldRecordingLocation, 2, 4);

        root.add(lbTags, 0, 5);
        root.add(textAreaTags, 1, 5, 2, 1);

        final FlowPane checkBoxes = new FlowPane();
        checkBoxes.getChildren().addAll(rbMythicplus, rbRaid);
        root.add(new Label("Mode"), 0, 6);
        root.add(checkBoxes, 1, 6, 2, 1);

        root.add(new Label("Send to Discord"), 0, 7);
        root.add(new FlowPane(cbSendLog, cbSendRecording), 1, 7, 2, 1);


        root.add(buttonBox, 0, 8, 3, 1);

        GridPane.setValignment(lbTags, VPos.TOP);
        GridPane.setVgrow(textAreaTags, Priority.ALWAYS);
        GridPane.setHgrow(textAreaTags, Priority.ALWAYS);
        final ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), column);
        root.setHgap(10);
        root.setVgap(10);
        root.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #ffffff");

        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(btSave, btCancel);

        final Scene scene = new Scene(root);
        setScene(scene);
        setResizable(false);

        textFieldName.setOnAction(e -> prefill());
        textFieldName.setPromptText("Type prefill to add some basic tags");

    }

    public static Optional<RecordEntry> getResult() {
        if (instance.isShowing()) {
            instance.toFront();
        } else {
            instance.clear();
            instance.showAndWait();
        }
        if (!instance.dialogResult) {
            return Optional.empty();
        }

        return Optional.of(instance.getData());
    }

    public static void edit(RecordEntry entry) {
        instance.textFieldName.setText(entry.getName());
        instance.textFieldRecordDate.setValue(entry.getRecordDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        instance.textFieldWarcraftLogsLink.setText(entry.getLogLink());
        instance.textFieldRecordingLocation.setText(entry.getVideoLink());
        instance.textAreaTags.setText(entry.getTags().replace(";", "\n"));

        if (entry.getTags().contains("raid")) {
            instance.rbRaid.setSelected(true);
        } else if (entry.getTags().contains("mythic+")) {
            instance.rbMythicplus.setSelected(true);
        }
        instance.showAndWait();

        if (instance.dialogResult) {
            entry.setName(instance.textFieldName.getText());
            entry.setRecordDate(Date.from(instance.textFieldRecordDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            entry.setLogLink(instance.textFieldWarcraftLogsLink.getText());
            entry.setVideoLink(instance.textFieldRecordingLocation.getText());
            entry.setTags(instance.textAreaTags.getText().replace("\n", ";"));
        }
    }

    public static void loadStylesheet(URL url) {
        instance.getScene().getStylesheets().clear();
        instance.getScene().getStylesheets().add(url.toExternalForm());
    }

    private void clear() {
        textFieldName.clear();
        textFieldRecordDate.setValue(LocalDate.now());
        textFieldWarcraftLogsLink.clear();
        textAreaTags.clear();
        textFieldRecordingLocation.clear();
    }

    private void cancel() {
        instance.dialogResult = false;
        this.close();
    }

    private void save() {
        dialogResult = true;
        if (textFieldName.getText().isEmpty()) {
            AlertUtils.showNormal("Name is empty", "Enter a valid name for the recording");
            return;
        }

        final boolean isRecordingLinkEmpty = textFieldRecordingLocation.getText().isEmpty();
        final boolean isLogLinkEmpty = textFieldWarcraftLogsLink.getText().isEmpty();

        if (isRecordingLinkEmpty || isLogLinkEmpty) {
            String message = "Do you want to continue without these fields?\n"
                    + (isRecordingLinkEmpty ? "• Recording is missing\n" : "")
                    + (isLogLinkEmpty ? "• Link to a log site is missing" : "");
            if (!AlertUtils.showConfirmation("Some optional  fields are empty", message)) {
                return;
            }
        }
        this.close();
        if (!textFieldWarcraftLogsLink.getText().isEmpty() && cbSendLog.isSelected()) {
            DiscordApi.getInstance().sendLogWebhook(textFieldWarcraftLogsLink.getText());
        }
        if (!textFieldRecordingLocation.getText().isEmpty() && cbSendRecording.isSelected()) {
            DiscordApi.getInstance().sendRecordWebhook(textFieldRecordingLocation.getText(), textFieldName.getText());
        }
    }

    private void selectRecordingFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video files", "*.mp4", "*.avi", "*.mkv", "*.mov"));
        var result = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (null != result) {
            textFieldRecordingLocation.setText(result.getAbsolutePath());
        }
    }

    private void prefill() {
        boolean isRaid = modeGroup.getSelectedToggle() == rbRaid;
        if (textFieldName.getText().equalsIgnoreCase("prefill")) {
            textFieldRecordDate.setValue(LocalDate.now());
            final LocalDate date = textFieldRecordDate.getValue();
            textAreaTags.setText(date
                    + "\n" + (isRaid ? "raid" : "mythic+")
                    + "\n" + System.currentTimeMillis());
            textFieldName.setText(date.toString());

        }
    }

    private RecordEntry getData() {
        final RecordEntry recordEntry = new RecordEntry();
        recordEntry.setName(textFieldName.getText());
        recordEntry.setRecordDate(Date.from(textFieldRecordDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        recordEntry.setLogLink(textFieldWarcraftLogsLink.getText());
        recordEntry.setTags(textAreaTags.getText().replace("\n", ";"));
        recordEntry.setVideoLink(textFieldRecordingLocation.getText());
        return recordEntry;
    }
}
