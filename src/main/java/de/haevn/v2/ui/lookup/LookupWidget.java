package de.haevn.v2.ui.lookup;

import de.haevn.jfx.mvc.IJfxWidget;
import de.haevn.mvc.IController;
import de.haevn.mvc.IModel;
import de.haevn.mvc.IView;

public class LookupWidget implements IJfxWidget {
    private final LookupView view = new LookupView();
    private final LookupController controller = new LookupController();


    @Override
    public String getName() {
        return "WeekWidget";
    }

    @Override
    public IController getController() {
        return controller;
    }

    @Override
    public IModel getModel() {
        return null;
    }

    @Override
    public IView getView() {
        return view;
    }
}
