package de.haevn.v2.ui.week;

import de.haevn.mvc.IController;
import de.haevn.mvc.IView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

class WeekController implements IController{
    private WeekView view;

    private SimpleStringProperty lbAffix = new SimpleStringProperty("");
    @Override
    public void link(IView view) {
        if(!(view instanceof WeekView))
            throw new IllegalArgumentException("View must be of type WeekView");

        this.view = (WeekView) view;



        init();
    }

    public void update(){
        Platform.runLater(() -> {
        });
    }

    public void init(){

        view.bindLbAffix(lbAffix);
    }
}
