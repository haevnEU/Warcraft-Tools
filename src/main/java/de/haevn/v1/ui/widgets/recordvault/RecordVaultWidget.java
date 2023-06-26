package de.haevn.v1.ui.widgets.recordvault;

import de.haevn.v1.abstraction.IViewWidget;

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
