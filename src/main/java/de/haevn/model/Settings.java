package de.haevn.model;

import de.haevn.enumeration.RegionEnum;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.*;

public class Settings {
    private static final Settings INSTANCE = new Settings();
    private final SimpleObjectProperty<RegionEnum> regionProperty = new SimpleObjectProperty<>(RegionEnum.EU);
    private final SimpleStringProperty themeProperty = new SimpleStringProperty("");
    private final StringProperty languageProperty = new SimpleStringProperty("");
    private final BooleanProperty licenseProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty autoUpdateProperty = new SimpleBooleanProperty(false);
    private final LongProperty apiRefreshProperty = new SimpleLongProperty(0);
    private final LongProperty networkTimeoutProperty = new SimpleLongProperty(0);
    private Settings() {
    }

    public static Settings getInstance() {
        return INSTANCE;
    }

    public void load() {
        PropertyHandler.getInstance("config").getOptional("app.region").ifPresent(region -> regionPropertyProperty().set(RegionEnum.getRegionByName(region)));
        PropertyHandler.getInstance("config").getOptional("app.theme").ifPresent(themeProperty::set);
        PropertyHandler.getInstance("config").getOptional("app.language").ifPresent(languageProperty::set);
        PropertyHandler.getInstance("config").getOptionalBoolean("app.license").ifPresent(licenseProperty::set);
        PropertyHandler.getInstance("config").getOptionalBoolean("app.autoUpdate").ifPresent(autoUpdateProperty::set);
        PropertyHandler.getInstance("config").getOptionalLong("network.refresh").ifPresent(networkTimeoutProperty::set);
        PropertyHandler.getInstance("config").getOptionalLong("network.timeout").ifPresent(apiRefreshProperty::set);
    }

    public void store() {
        PropertyHandler.getInstance("config").store();
    }

    public SimpleObjectProperty<RegionEnum> regionPropertyProperty() {
        return regionProperty;
    }

    public SimpleStringProperty themePropertyProperty() {
        return themeProperty;
    }

    public StringProperty languagePropertyProperty() {
        return languageProperty;
    }

    public BooleanProperty licensePropertyProperty() {
        return licenseProperty;
    }

    public BooleanProperty autoUpdatePropertyProperty() {
        return autoUpdateProperty;
    }

    public LongProperty apiRefreshPropertyProperty() {
        return apiRefreshProperty;
    }

    public LongProperty networkTimeoutPropertyProperty() {
        return networkTimeoutProperty;
    }
}
