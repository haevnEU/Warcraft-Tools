package de.haevn.ui.widgets.recordarchive;

import de.haevn.abstraction.IViewWidget;

public class RecordArchiveWidget implements IViewWidget {
    private final RecordArchiveView view = new RecordArchiveView();

    public RecordArchiveWidget() {
        new RecordArchiveController().link(view, null);
    }

    @Override
    public RecordArchiveView getView() {
        return view;
    }
}
