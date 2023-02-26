package de.haevn.ui.widgets.recordvault;

import de.haevn.abstraction.IViewWidget;

public class RecordVaultWidget implements IViewWidget {
    private final RecordVaultView view = new RecordVaultView();

    public RecordVaultWidget() {
        new RecordVaultController().link(view, null);
    }

    @Override
    public RecordVaultView getView() {
        return view;
    }
}
