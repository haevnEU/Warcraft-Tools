package de.haevn.ui.widgets.currentweek;

import de.haevn.abstraction.IViewWidget;

public class CurrentWeekWidget implements IViewWidget {
    private final CurrentWeekView view = new CurrentWeekView();
    private final CurrentWeekController controller = new CurrentWeekController();

    public CurrentWeekWidget(){
        controller.link(view, null);
    }

    @Override
    public CurrentWeekView getView() {
        return view;
    }
}
