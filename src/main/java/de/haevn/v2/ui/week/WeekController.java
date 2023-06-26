package de.haevn.v2.ui.week;

import de.haevn.mvc.IController;
import de.haevn.mvc.IView;
import de.haevn.v2.ui.UiSettings;

class WeekController implements IController{
    private WeekView view;
    @Override
    public void link(IView view) {
        if(!(view instanceof WeekView))
            throw new IllegalArgumentException("View must be of type WeekView");

        this.view = (WeekView) view;
    }
}
