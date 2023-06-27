package de.haevn.v2.ui.lookup;

import de.haevn.mvc.IView;
import de.haevn.jfx.html.H4;import javafx.scene.layout.GridPane;

class LookupView extends GridPane implements IView {

    public LookupView() {
        add(new H4("LookupView"), 0, 0);
    }

}
