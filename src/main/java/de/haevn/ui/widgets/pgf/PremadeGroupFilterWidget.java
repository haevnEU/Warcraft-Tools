package de.haevn.ui.widgets.pgf;

import de.haevn.abstraction.IViewWidget;

public class PremadeGroupFilterWidget implements IViewWidget {
    private final PremadeGroupFilterView view = new PremadeGroupFilterView();

    public PremadeGroupFilterWidget(){
    }

    @Override
    public PremadeGroupFilterView getView() {
        return view;
    }
}
