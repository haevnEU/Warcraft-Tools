package de.haevn.ui.widgets.search;

import de.haevn.abstraction.IViewWidget;

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
