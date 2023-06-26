package de.haevn.v1.ui.widgets.settings;

import de.haevn.v1.abstraction.IViewWidget;

public class SettingsWidget implements IViewWidget {
    private final SettingsView view = new SettingsView();


    @Override
    public SettingsView getView() {
        return view;
    }
}
