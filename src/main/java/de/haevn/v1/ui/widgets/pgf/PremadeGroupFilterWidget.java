package de.haevn.v1.ui.widgets.pgf;

import de.haevn.v1.abstraction.IViewWidget;

public class PremadeGroupFilterWidget implements IViewWidget {
    private final PremadeGroupFilterView view = new PremadeGroupFilterView();


    @Override
    public PremadeGroupFilterView getView() {
        return view;
    }
}
