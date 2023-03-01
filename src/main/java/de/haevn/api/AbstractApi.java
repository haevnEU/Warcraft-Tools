package de.haevn.api;

import de.haevn.enumeration.RegionEnum;
import de.haevn.model.Settings;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AbstractApi {
    protected final PropertyHandler propertyHandler = PropertyHandler.getInstance("config");
    protected final PropertyHandler urlHandler = PropertyHandler.getInstance("urls");
    protected final SimpleObjectProperty<RegionEnum> region = new SimpleObjectProperty<>();
    protected final SimpleLongProperty refreshDuration = new SimpleLongProperty();

    protected AbstractApi() {
        reload();
    }

    public void reload() {
        region.bindBidirectional(Settings.getInstance().regionPropertyProperty());
        refreshDuration.bindBidirectional(Settings.getInstance().apiRefreshPropertyProperty());
    }

}
