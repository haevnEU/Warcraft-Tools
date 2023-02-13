package de.haevn.api;

import de.haevn.enumeration.RegionEnum;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.SimpleObjectProperty;

public class AbstractApi {
    protected final PropertyHandler propertyHandler = PropertyHandler.getInstance("config");
    protected final PropertyHandler urlHandler = PropertyHandler.getInstance("urls");
    protected final SimpleObjectProperty<RegionEnum> region = new SimpleObjectProperty<>(RegionEnum.getRegionByName(propertyHandler.get("region")));
    protected long refreshDuration;

    protected AbstractApi() {
        reload();
    }

    public void reload() {
        refreshDuration = propertyHandler.getLong("network.refresh");

    }

}
