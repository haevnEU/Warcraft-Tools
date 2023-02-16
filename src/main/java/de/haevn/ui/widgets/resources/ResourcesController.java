package de.haevn.ui.widgets.resources;


import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.api.RaiderIOApi;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.SimpleStringProperty;

import java.util.function.Consumer;

class ResourcesController implements IController {

    private final static String MYTHICPL_US_KEY = "resources.mythicplus";
    private final static String KEYSTONE_HEROES_KEY = "resources.keystone_heroes";

    private final static String QUESTIONABLE_EPIC_KEY = "resources.questionablyepic";

    private final static String MYTHIC_PLUS_SUBCREATION_KEY = "resources.mythicplus_subcreation";

    private ResourcesView view;

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof ResourcesView)) {
            throw new IllegalArgumentException("View must be of type LookupView");
        }
        this.view = (ResourcesView) view;

        this.view.addResource("mythicpl.us", PropertyHandler.getInstance("urls").get(MYTHICPL_US_KEY));
        this.view.addResource("keystone hero", PropertyHandler.getInstance("urls").get(KEYSTONE_HEROES_KEY));
        this.view.addResource("Questionable Epic", PropertyHandler.getInstance("urls").get(QUESTIONABLE_EPIC_KEY));
        this.view.addResource("Mythic+ Subcreation", PropertyHandler.getInstance("urls").get(MYTHIC_PLUS_SUBCREATION_KEY));

    }

}
