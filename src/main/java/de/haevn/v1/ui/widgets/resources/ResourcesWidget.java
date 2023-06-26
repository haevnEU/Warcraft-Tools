package de.haevn.v1.ui.widgets.resources;

import de.haevn.v1.abstraction.IViewWidget;
import de.haevn.v1.model.WarcraftResources;

import java.util.List;

public class ResourcesWidget implements IViewWidget {
    private final ResourcesView view = new ResourcesView();
    private final ResourcesController controller;

    public ResourcesWidget() {
        this(null);
    }

    public ResourcesWidget(List<WarcraftResources.Resource> resourceList) {
        controller = new ResourcesController(resourceList);
        controller.link(view, null);
    }

    @Override
    public ResourcesView getView() {
        return view;
    }

    public boolean isResourceAvailable() {
        return controller.isResourceAvailable();
    }
}
