package de.haevn.ui.widgets.recordarchive;

import de.haevn.abstraction.IViewWidget;

public class RecordArchiveWidget implements IViewWidget {
    private final RecordArchiveView view = new RecordArchiveView();
    private final RecordArchiveController controller = new RecordArchiveController();

    public RecordArchiveWidget(){
        controller.link(view, null);
    }

    @Override
    public RecordArchiveView getView() {
        return view;
    }
}
