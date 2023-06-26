package de.haevn.v1.ui.widgets.currentweek;

import de.haevn.v1.abstraction.IViewWidget;

public class CurrentWeekWidget implements IViewWidget {
    private final CurrentWeekView view = new CurrentWeekView();

    public CurrentWeekWidget() {
        new CurrentWeekController().link(view, null);
    }

    @Override
    public CurrentWeekView getView() {
        return view;
    }
}
