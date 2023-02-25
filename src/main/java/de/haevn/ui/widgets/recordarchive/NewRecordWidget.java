package de.haevn.ui.widgets.recordarchive;

import de.haevn.model.recording.RecordEntry;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.AlertUtils;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

    private boolean dialogResult = false;


    private NewRecordWidget() {
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

        root.add(buttonBox, 0, 6, 3, 1);

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

        textFieldRecordingLocation.setEditable(false);
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

        if(textFieldName.getText().isEmpty()){
            AlertUtils.showNormal("Name is empty", "Enter a valid name for the recording");
        } else if(textFieldRecordingLocation.getText().isEmpty()){
            AlertUtils.showNormal("Recording location is empty", "Please choose the location of the recording");
        }else if(textFieldWarcraftLogsLink.getText().isEmpty()){
            if(AlertUtils.showConfirmation("Name is empty", "Enter a valid name for the recording")){
                this.close();
            }
        }else{
            this.close();
        }
    }

    private void selectRecordingFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Video files", "*.mp4", "*.avi", "*.mkv", "*.mov"));
        var result = fileChooser.showOpenDialog(this.getScene().getWindow());
        textFieldRecordingLocation.setText(result.getAbsolutePath());
    }

    private void prefill() {
        if (textFieldName.getText().equalsIgnoreCase("prefill")) {
            textFieldRecordDate.setValue(LocalDate.now());
            final LocalDate date = textFieldRecordDate.getValue();
            textAreaTags.setText(date
                    + "\n" + date.getYear()
                    + "\n" + date.getMonth()
                    + "\n" + date.getDayOfMonth()
                    + "\n" + date.getDayOfWeek()
                    + "\nrecording"
                    + "\nraid"
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
