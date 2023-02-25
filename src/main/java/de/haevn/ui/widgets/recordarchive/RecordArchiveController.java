package de.haevn.ui.widgets.recordarchive;

import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.model.recording.RecordEntry;
import de.haevn.utils.FileIO;
import de.haevn.utils.JsonAndStringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

import java.io.File;
import java.util.Date;

class RecordArchiveController implements IController {

    private RecordArchiveView view;

    private ObservableList<RecordEntry> entries = FXCollections.observableArrayList();

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof RecordArchiveView)) {
            throw new IllegalArgumentException("View is not of type DungeonView");
        }
        this.view = (RecordArchiveView) view;

        this.view.setOnAddNewEntry(this::addNewEntry);
        this.view.bindRecordArchiveList(entries);
        this.view.setOnDeleteEntry(this::deleteEntry);

        load();
        Runtime.getRuntime().addShutdownHook(new Thread(this::store));
    }

    private void deleteEntry() {
        entries.remove(view.getSelectedEntry());
    }

    int cnt = 0;
    private void addNewEntry() {
        NewRecordWidget.getResult().ifPresent(entries::add);
    }


    private void load(){
        String json = FileIO.readFile("json/recording.json");
        JsonAndStringUtils.parseSecure(json, RecordEntry[].class).ifPresent(entries::addAll);
    }

    private void store(){
        JsonAndStringUtils.exportJson(entries).ifPresent(data -> FileIO.store("json/recording.json", data));
    }
}
