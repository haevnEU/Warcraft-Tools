package de.haevn.ui.widgets.resources;

import de.haevn.abstraction.IViewWidget;
import de.haevn.utils.WarcraftResources;

import java.util.List;

public class ResourcesWidget implements IViewWidget {
    private final ResourcesView view = new ResourcesView();
    private final ResourcesController controller;

    public ResourcesWidget(List<WarcraftResources.Resource> resourceList){
        controller = new ResourcesController(resourceList);
        controller.link(view, null);
    }

    @Override
    public ResourcesView getView() {
        return view;
    }
}
