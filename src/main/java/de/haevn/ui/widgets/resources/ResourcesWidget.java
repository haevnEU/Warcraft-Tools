package de.haevn.ui.widgets.resources;

import de.haevn.abstraction.IViewWidget;

public class ResourcesWidget implements IViewWidget {
    private final ResourcesView view = new ResourcesView();
    private final ResourcesController controller = new ResourcesController();

    public ResourcesWidget(){
        controller.link(view, null);
    }

    @Override
    public ResourcesView getView() {
        return view;
    }
}
