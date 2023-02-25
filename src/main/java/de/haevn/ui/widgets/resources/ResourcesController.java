package de.haevn.ui.widgets.resources;


import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.utils.WarcraftResources;

import java.util.ArrayList;
import java.util.List;

class ResourcesController implements IController {

    public ResourcesController(List<WarcraftResources.Resource> resource) {
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

        this.resources.forEach(resource -> this.view.addResource(resource.getName(), resource.getUrl()));
    }

    public boolean isResourceAvailable() {
        return null != resources && !resources.isEmpty();
    }
}
