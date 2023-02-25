package de.haevn.ui.widgets.recordarchive;

import de.haevn.Main;
import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.model.recording.RecordEntry;
import de.haevn.utils.FileIO;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.Network;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class RecordArchiveController implements IController {

    private final ObservableList<RecordEntry> entries = FXCollections.observableArrayList();
    int cnt = 0;
    private RecordArchiveView view;

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof RecordArchiveView)) {
            throw new IllegalArgumentException("View is not of type DungeonView");
        }
        this.view = (RecordArchiveView) view;

        this.view.setOnAddNewEntry(this::addNewEntry);
        this.view.bindRecordArchiveList(entries);
        this.view.setOnDeleteEntry(this::deleteEntry);
        this.view.setOnSelectionChanged(this::onSelectionChanged);

        load();
        Runtime.getRuntime().addShutdownHook(new Thread(this::store));
    }

    private void onSelectionChanged(ObservableValue<? extends RecordEntry> observable, RecordEntry old, RecordEntry entry) {
        view.displayRecord(entry);
        if (null != entry.getVideoLink() && !entry.getVideoLink().isEmpty()) {
            view.setOnButtonViewVideoClicked(e -> openVideo(entry.getVideoLink()));
        } else {
            view.setOnButtonViewVideoClicked(null);
        }
        if (null != entry.getLogLink() && Network.isUrl(entry.getLogLink())) {
            view.setOnButtonViewLogsClicked(e -> Main.openWebsite(entry.getLogLink()));
        } else {
            view.setOnButtonViewLogsClicked(null);
        }
    }

    private void deleteEntry() {
        entries.remove(view.getSelectedEntry());
    }

    private void addNewEntry() {
        NewRecordWidget.getResult().ifPresent(entries::add);
    }


    private void load() {
        String json = FileIO.readFile("json/recording.json");
        JsonAndStringUtils.parseSecure(json, RecordEntry[].class).ifPresent(entries::addAll);
    }

    private void store() {
        JsonAndStringUtils.exportJson(entries).ifPresent(data -> FileIO.store("json/recording.json", data));
    }

    private void openVideo(String url) {
        if (Network.isUrl(url)) {
            Main.openWebsite(url);
        } else {
            try {
                Desktop.getDesktop().open(new File(url));
            } catch (IOException ignored) {
            }
        }
    }

}
