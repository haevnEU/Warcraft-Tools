package de.haevn.v1.ui.widgets.dungeon;

import de.haevn.v1.abstraction.IViewWidget;

public class DungeonWidget implements IViewWidget {
    private final DungeonView view = new DungeonView();

    public DungeonWidget() {
        new DungeonController().link(view, null);
    }

    @Override
    public DungeonView getView() {
        return view;
    }
}
