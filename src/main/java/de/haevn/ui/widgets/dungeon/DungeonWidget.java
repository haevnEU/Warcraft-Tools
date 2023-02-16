package de.haevn.ui.widgets.dungeon;

import de.haevn.abstraction.IViewWidget;

public class DungeonWidget implements IViewWidget {
    private final DungeonView view = new DungeonView();
    private final DungeonController controller = new DungeonController();

    public DungeonWidget(){
        controller.link(view, null);
    }

    @Override
    public DungeonView getView() {
        return view;
    }
}
