package de.haevn.api;

import de.haevn.enumeration.RegionEnum;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.PropertyKeys;
import javafx.beans.property.SimpleObjectProperty;

public class AbstractApi {
    protected final PropertyHandler propertyHandler = PropertyHandler.getInstance("config");
    protected final PropertyHandler urlHandler = PropertyHandler.getInstance("urls");
    protected final SimpleObjectProperty<RegionEnum> region = new SimpleObjectProperty<>(RegionEnum.getRegionByName(propertyHandler.get(PropertyKeys.APP_REGION)));
    protected long refreshDuration;

    protected AbstractApi() {
        reload();
    }

    public void reload() {
        refreshDuration = propertyHandler.getLong(PropertyKeys.NETWORK_REFRESH);

    }

}
