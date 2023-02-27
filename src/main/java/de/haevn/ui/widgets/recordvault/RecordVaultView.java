package de.haevn.ui.widgets.recordvault;

import de.haevn.abstraction.IView;
import de.haevn.model.recording.RecordEntry;
import de.haevn.ui.elements.ImageButton;
import de.haevn.ui.elements.html.AH2;
import de.haevn.ui.elements.html.H1;
import de.haevn.utils.CustomStringUtils;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.PropertyHandler;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.function.Predicate;

class RecordVaultView extends BorderPane implements IView {

    public final TextField tfQuery = new TextField();

    private final ImageButton btDelete = ImageButton.createCrossButton();

    private final Button btViewLog = new Button("Warcraftlogs");
    private final Button btViewRecording = new Button("Recording");
    private final ImageButton btSendLog = ImageButton.createDiscordButton();
    private final ImageButton btSendRecording = ImageButton.createDiscordButton();

    private final Button btAddNewEntry = new Button("Add new record");
    private final AH2 title = new AH2();
    private final Label lbDate = new Label();
    private final TextArea taTags = new TextArea();
    private final ListView<RecordEntry> lvRecordArchive = new ListView<>();


    // Add a date filter
    private final Predicate<RecordEntry> nameFilter = recordEntry -> recordEntry.getName().toLowerCase().contains(tfQuery.getText().toLowerCase());
    private final Predicate<RecordEntry> tagFilter = recordEntry ->
            Arrays.stream(recordEntry.getTags().toLowerCase().split(";"))
                    .toList()
                    .stream()
                    .anyMatch(s -> s.contains(tfQuery.getText().toLowerCase()));


    private FilteredList<RecordEntry> filteredData;


    RecordVaultView() {
        setTop(new H1("Gameplay recording vault"));

        tfQuery.setPromptText("Enter a name or tag to filter");
        final VBox leftBox = new VBox(btAddNewEntry, tfQuery, lvRecordArchive);
        leftBox.setSpacing(10);
        VBox.setVgrow(lvRecordArchive, Priority.ALWAYS);
        final GridPane centerPane = new GridPane();
        final ColumnConstraints column1 = new ColumnConstraints();
        final ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(20);
        column2.setPercentWidth(70);
        centerPane.getColumnConstraints().addAll(column1, column2);

        final Label lbTags = new Label("Tags");

        final HBox viewButtonBox = new HBox();
        final HBox sendButtonBox = new HBox();
        final VBox buttonBox = new VBox(viewButtonBox, sendButtonBox);
        buttonBox.setSpacing(5);

        centerPane.add(title, 0, 0, 2, 1);

        centerPane.add(buttonBox, 0, 1, 2, 1);

        centerPane.add(new Label("Date"), 0, 3);
        centerPane.add(lbDate, 1, 3);

        centerPane.add(lbTags, 0, 4);
        centerPane.add(taTags, 1, 4);

        viewButtonBox.getChildren().addAll(btViewRecording, btSendRecording, btViewLog, btSendLog, btDelete);
        viewButtonBox.setSpacing(10);

        centerPane.setVisible(false);
        setCenter(centerPane);
        setLeft(leftBox);
        setPadding(new Insets(10));
        lvRecordArchive.setItems(filteredData);

        tfQuery.textProperty().addListener((observable, old, entry) -> {
            if (entry.isEmpty()) {
                filteredData.setPredicate(p -> true);
            } else {
                filteredData.setPredicate(nameFilter.or(tagFilter));
            }
        });

        GridPane.setValignment(lbTags, VPos.TOP);
        taTags.setEditable(false);

        btViewLog.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                CustomStringUtils.copy(getSelectedEntry().getLogLink());
            }
        });
        btViewRecording.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                CustomStringUtils.copy(getSelectedEntry().getVideoLink());
            }
        });
    }

    public void setOnAddNewEntry(Runnable runnable) {
        btAddNewEntry.setOnAction(e -> runnable.run());
    }

    public void bindRecordArchiveList(ObservableList<RecordEntry> recordEntries) {
        filteredData = new FilteredList<>(recordEntries, p -> true);
        lvRecordArchive.setItems(filteredData);
    }

    public void setOnSelectionChanged(ChangeListener<RecordEntry> event) {
        lvRecordArchive.getSelectionModel().selectedItemProperty().addListener(event);
    }

    public void setOnButtonViewVideoClicked(Runnable runnable) {
        btViewRecording.setOnAction(e -> runnable.run());
    }

    public void setOnButtonViewLogsClicked(Runnable runnable) {
        btViewLog.setOnAction(e -> runnable.run());
    }

    public void displayRecord(RecordEntry recordEntry) {
        if (null == recordEntry) {
            getCenter().setVisible(false);
            return;
        }
        getCenter().setVisible(true);
        title.setText(recordEntry.getName());
        title.setLink(recordEntry.getLogLink());

        lbDate.setText(recordEntry.getRecordDate().toString());
        taTags.setText(recordEntry.getTags().replace(";", "\n"));

        btViewRecording.setDisable(recordEntry.getVideoLink().isEmpty());

        btViewLog.setDisable(recordEntry.getLogLink().isEmpty());

        btSendRecording.setDisable(true);
        btSendLog.setDisable(true);

        PropertyHandler.getInstance("config").getOptional("urls.webhook.log").ifPresent(e -> {
            btSendRecording.setDisable(!NetworkUtils.isUrl(recordEntry.getVideoLink()));
            btSendLog.setDisable(!NetworkUtils.isUrl(recordEntry.getLogLink()));
        });

    }

    public void setOnSendLog(Runnable runnable) {
        btSendLog.setOnAction(e -> runnable.run());
    }

    public void setOnSendRecording(Runnable runnable) {
        btSendRecording.setOnAction(e -> runnable.run());
    }

    public void setOnDeleteEntry(Runnable runnable) {
        btDelete.setOnAction(e -> runnable.run());
    }

    public RecordEntry getSelectedEntry() {
        return lvRecordArchive.getSelectionModel().getSelectedItem();
    }
}
