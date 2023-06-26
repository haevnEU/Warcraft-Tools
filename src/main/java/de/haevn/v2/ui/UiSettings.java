package de.haevn.v2.ui;

import de.haevn.mvc.ISettings;
import javafx.beans.property.SimpleObjectProperty;

public class UiSettings implements ISettings {
    private static final UiSettings INSTANCE = new UiSettings();

    public synchronized static UiSettings getInstance() {
        return INSTANCE;
    }

    private UiSettings() {
    }


    private final SimpleObjectProperty<?> regionProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<?> languageProperty = new SimpleObjectProperty<>();

    public SimpleObjectProperty<?> region() {
        return regionProperty;
    }

    public SimpleObjectProperty<?> language() {
        return languageProperty;
    }
}
