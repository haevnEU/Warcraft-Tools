package de.haevn.ui.widgets.search;

import de.haevn.abstraction.IViewWidget;

public class PlayerSearchWidget implements IViewWidget {
    private final PlayerSearchView view = new PlayerSearchView();
    private final PlayerSearchController controller = new PlayerSearchController();

    public PlayerSearchWidget(){
        controller.link(view, null);
    }

    @Override
    public PlayerSearchView getView() {
        return view;
    }
}
