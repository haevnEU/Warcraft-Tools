package de.haevn.ui.widgets.recordarchive;

import de.haevn.abstraction.IView;
import de.haevn.model.recording.RecordEntry;
import de.haevn.ui.elements.html.AH2;
import de.haevn.ui.elements.html.H1;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.function.Predicate;

class RecordArchiveView extends BorderPane implements IView {

    public final TextField tfQuery = new TextField();
    private final Button btViewLog = new Button("Warcraftlogs");
    private final Button btViewRecording = new Button("Recording");
    private final Button btAddNewEntry = new Button("Add new record");
    private final Button btDelete = new Button("Delete");
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


    RecordArchiveView() {
        setTop(new H1("Gameplay recording vault"));

        tfQuery.setPromptText("Enter a name or tag to filter");
        final VBox leftBox = new VBox(btAddNewEntry, tfQuery, lvRecordArchive);
        final GridPane centerPane = new GridPane();
        final ColumnConstraints column1 = new ColumnConstraints();
        final ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(20);
        column2.setPercentWidth(70);
        centerPane.getColumnConstraints().addAll(column1, column2);

        final Label lbTags = new Label("Tags");
        final HBox buttonBox = new HBox();

        centerPane.add(title, 0, 0, 2, 1);
        centerPane.add(new Label("Date"), 0, 3);
        centerPane.add(lbTags, 0, 4);

        centerPane.add(lbDate, 1, 3);
        centerPane.add(taTags, 1, 4);

        buttonBox.getChildren().addAll(btViewRecording, btViewLog, btDelete);
        buttonBox.setSpacing(10);
        centerPane.add(buttonBox, 0, 1, 2, 1);

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

    public void setOnButtonViewVideoClicked(EventHandler<ActionEvent> event) {
        if (null == event) {
            btViewRecording.setDisable(true);
            return;
        }
        btViewRecording.setDisable(false);
        btViewRecording.setOnAction(event);
    }

    public void setOnButtonViewLogsClicked(EventHandler<ActionEvent> event) {
        if (null == event) {
            btViewLog.setDisable(true);
            return;
        }
        btViewLog.setDisable(false);
        btViewLog.setOnAction(event);
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
    }


    public void setOnDeleteEntry(Runnable runnable) {
        btDelete.setOnAction(e -> runnable.run());
    }

    public RecordEntry getSelectedEntry() {
        return lvRecordArchive.getSelectionModel().getSelectedItem();
    }
}
