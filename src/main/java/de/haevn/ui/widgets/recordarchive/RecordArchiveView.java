package de.haevn.ui.widgets.recordarchive;

import de.haevn.Main;
import de.haevn.abstraction.IView;
import de.haevn.model.dungeons.Enemy;
import de.haevn.model.recording.RecordEntry;
import de.haevn.ui.elements.ReadOnlyTextField;
import de.haevn.ui.elements.html.AH1;
import de.haevn.ui.elements.html.AH2;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.Network;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

class RecordArchiveView extends BorderPane implements IView {


    private final ListView<RecordEntry> lvRecordArchive = new ListView<>();
    private final Button btAddNewEntry = new Button("Add new record");
    public final TextField tfQuery = new TextField();
    private FilteredList<RecordEntry> filteredData;

    private final Predicate<RecordEntry> nameFilter = recordEntry -> recordEntry.getName().toLowerCase().contains(tfQuery.getText().toLowerCase());
    //private final Predicate<RecordEntry> dateFilter = recordEntry -> recordEntry.getRecordDate().contains(tfQuery.getText().toLowerCase());
    private final Predicate<RecordEntry> dateFilter = recordEntry -> false;
    private final Predicate<RecordEntry> tagFilter = recordEntry -> Arrays.stream(recordEntry.getTags().toLowerCase().split(";")).toList().contains(tfQuery.getText().toLowerCase());

    private final Button btViewLog = new Button("Warcraftlogs");
    private final Button btViewRecording = new Button("Recording");
    private final Button btDelete = new Button("Delete");

    private final Label lbDate = new Label();
    private final TextArea taTags = new TextArea();

    private final AH2 title = new AH2();

    RecordArchiveView() {
        setTop(new H1("Gameplay recording vault"));
        //tfQuery.sethint("Enter query here");

        final VBox leftBox = new VBox(btAddNewEntry, tfQuery, lvRecordArchive);
        final GridPane centerPane = new GridPane();
        final ColumnConstraints column1 = new ColumnConstraints();
        final ColumnConstraints column2 = new ColumnConstraints();
        column1.setPercentWidth(20);
        column2.setPercentWidth(70);
        centerPane.getColumnConstraints().addAll(column1, column2);


        centerPane.add(title, 0, 0, 2, 1);
        final Label lbTags = new Label("Tags");
        centerPane.add(new Label("Date"), 0, 3);
        centerPane.add(lbTags, 0, 4);

        centerPane.add(lbDate, 1, 3);
        centerPane.add(taTags, 1, 4);

        final HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(btViewRecording, btViewLog, btDelete);
        buttonBox.setSpacing(10);
        centerPane.add(buttonBox, 0, 1, 2, 1);

        centerPane.setVisible(false);
        setCenter(centerPane);
        setLeft(leftBox);

        lvRecordArchive.setItems(filteredData);
        tfQuery.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(nameFilter.or(tagFilter).or(dateFilter));
        });

GridPane.setValignment(lbTags, VPos.TOP);
        taTags.setEditable(false);

        lvRecordArchive.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RecordEntry>() {
            @Override
            public void changed(ObservableValue<? extends RecordEntry> observable, RecordEntry oldValue, RecordEntry newValue) {
                if (newValue != null) {
                    centerPane.setVisible(true);
                    title.setText(newValue.getName());
                    title.setLink(newValue.getLogLink());

                    lbDate.setText(newValue.getRecordDate().toString());
                    taTags.setText(newValue.getTags().replace(";", "\n"));
                    EventHandler<ActionEvent> openVideo = e -> {};
                    if(null != newValue.getVideoLink() && !newValue.getVideoLink().isEmpty()) {
                        btViewRecording.setDisable(false);
                        openVideo = (e -> {
                            openVideo(newValue.getVideoLink());
                        });
                    } else {
                        btViewRecording.setDisable(true);
                    }
                    btViewRecording.setOnAction(openVideo);

                    EventHandler<ActionEvent> openLog = e -> {};
                    if(null != newValue.getLogLink() && Network.isUrl(newValue.getLogLink())) {
                        btViewLog.setDisable(false);
                        openLog = (e -> {
                            Main.openWebsite(newValue.getLogLink());
                        });
                    } else {
                        btViewLog.setDisable(true);
                    }
                    btViewLog.setOnAction(openLog);
                }else {
                    centerPane.setVisible(false);
                }
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

    private void openVideo(String url){
        if(Network.isUrl(url)) {
            Main.openWebsite(url);
        } else {
            try {
                Desktop.getDesktop().open(new File(url));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }
    }



    public void setOnDeleteEntry(Runnable runnable) {
        btDelete.setOnAction(e -> runnable.run());
    }

    public RecordEntry getSelectedEntry() {
        return lvRecordArchive.getSelectionModel().getSelectedItem();
    }
}
