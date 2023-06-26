package de.haevn.v1.ui.widgets.search;

import de.haevn.v1.abstraction.IViewWidget;

public class PlayerSearchWidget implements IViewWidget {
    private final PlayerSearchView view = new PlayerSearchView();

    public PlayerSearchWidget() {
        new PlayerSearchController().link(view, null);
    }

    @Override
    public PlayerSearchView getView() {
        return view;
    }
}
