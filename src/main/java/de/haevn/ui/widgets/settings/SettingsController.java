package de.haevn.ui.widgets.settings;

import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;

public class SettingsController implements IController {


    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof SettingsView settingsView)) {
            throw new IllegalArgumentException("View is not of type SettingsView");
        }


    }
}
