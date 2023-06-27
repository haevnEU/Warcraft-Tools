package de.haevn.v2.ui.week;

import de.haevn.jfx.mvc.IJfxWidget;
import de.haevn.mvc.IController;
import de.haevn.mvc.IModel;
import de.haevn.mvc.IView;

public class WeekWidget implements IJfxWidget {
    private final WeekView view = new WeekView();
    private final WeekController controller = new WeekController();

    public WeekWidget(){
        controller.link(view);
    }

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
