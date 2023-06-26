package de.haevn.v2.ui.lookup;

import de.haevn.mvc.IController;
import de.haevn.mvc.IView;


class LookupController implements IController{
    private LookupView view;
    @Override
    public void link(IView view) {
        if(!(view instanceof LookupView))
            throw new IllegalArgumentException("View must be of type WeekView");

        this.view = (LookupView) view;
    }
}
