package de.haevn.ui.widgets.settings;

import de.haevn.abstraction.IViewWidget;

public class SettingsWidget implements IViewWidget {
    private final SettingsView view = new SettingsView();


    @Override
    public SettingsView getView() {
        return view;
    }
}
