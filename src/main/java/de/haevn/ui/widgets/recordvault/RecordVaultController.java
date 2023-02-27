package de.haevn.ui.widgets.recordvault;

import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.api.DiscordApi;
import de.haevn.model.recording.RecordEntry;
import de.haevn.utils.FileIO;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.SerializationUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

class RecordVaultController implements IController {
    private final SimpleObjectProperty<RecordEntry> recordProperty = new SimpleObjectProperty<>();
    private final ObservableList<RecordEntry> entries = FXCollections.observableArrayList();
    private RecordVaultView view;

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof RecordVaultView)) {
            throw new IllegalArgumentException("View is not of type DungeonView");
        }
        this.view = (RecordVaultView) view;

        this.view.setOnAddNewEntry(this::addNewEntry);
        this.view.bindRecordArchiveList(entries);
        this.view.setOnDeleteEntry(this::deleteEntry);
        this.view.setOnSendLog(this::sendLog);
        this.view.setOnSendRecording(this::sendRecord);
        this.view.setOnSelectionChanged(((observable, oldValue, newValue) -> onSelectionChanged(newValue)));

        load();
        Runtime.getRuntime().addShutdownHook(new Thread(this::store));
    }

    private void sendRecord() {
        DiscordApi.getInstance().sendRecordWebhook(recordProperty.get().getVideoLink());
    }

    private void sendLog() {
        DiscordApi.getInstance().sendLogWebhook(recordProperty.get().getLogLink());

    }

    private void onSelectionChanged(RecordEntry entry) {

        recordProperty.set(entry);
        view.displayRecord(entry);
        if (null != entry.getVideoLink() && !entry.getVideoLink().isEmpty()) {
            view.setOnButtonViewVideoClicked(e -> openVideo(entry.getVideoLink()));
        } else {
            view.setOnButtonViewVideoClicked(null);
        }
        if (null != entry.getLogLink() && NetworkUtils.isUrl(entry.getLogLink())) {
            view.setOnButtonViewLogsClicked(e -> NetworkUtils.openWebsite(entry.getLogLink()));
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
        SerializationUtils.parseJsonSecure(json, RecordEntry[].class).ifPresent(entries::addAll);
    }

    private void store() {
        SerializationUtils.exportJson(entries).ifPresent(data -> FileIO.store("json/recording.json", data));
    }

    private void openVideo(String url) {
        if (NetworkUtils.isUrl(url)) {
            NetworkUtils.openWebsite(url);
        } else {
            FileIO.openDefaultApplication(new File(url));
        }
    }

}
