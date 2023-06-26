package de.haevn.v1.api;

import de.haevn.v1.enumeration.RegionEnum;
import de.haevn.v1.utils.PropertyHandler;
import de.haevn.v1.utils.PropertyKeys;
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
