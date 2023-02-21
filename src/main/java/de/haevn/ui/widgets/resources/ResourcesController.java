package de.haevn.ui.widgets.resources;


import com.fasterxml.jackson.core.JsonProcessingException;
import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.api.RaiderIOApi;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.utils.FileIO;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.WarcraftResources;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ResourcesController implements IController {

    public ResourcesController(List<WarcraftResources.Resource> resource){
        this.resources.addAll(resource);
    }

    private final List<WarcraftResources.Resource> resources = new ArrayList<>();

    private ResourcesView view;

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof ResourcesView)) {
            throw new IllegalArgumentException("View must be of type LookupView");
        }
        this.view = (ResourcesView) view;

        this.resources.forEach(resource -> {
            this.view.addResource(resource.getName(), resource.getUrl());
        });
    }

    public boolean isResourceAvailable() {
        return null != resources && !resources.isEmpty();
    }
}
