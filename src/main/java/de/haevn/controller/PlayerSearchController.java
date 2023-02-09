package de.haevn.controller;


import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.api.RaiderIOApi;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.views.PlayerSearchView;
import javafx.beans.property.SimpleStringProperty;

import java.util.function.Consumer;

public class PlayerSearchController implements IController {
    private final SimpleStringProperty realmProperty = new SimpleStringProperty("");
    private final SimpleStringProperty nameProperty = new SimpleStringProperty("");
    private PlayerSearchView view;


    @Override
    public void link(IView view, IModel model) {

        if (!(view instanceof PlayerSearchView)) {
            throw new IllegalArgumentException("View must be of type LookupView");
        }
        this.view = (PlayerSearchView) view;


        this.view.onButtonSearchClicked(e -> onButtonSearchClicked());
        realmProperty.bind(this.view.getRealm());
        nameProperty.bind(this.view.getName());
    }

    public void onButtonSearchClicked() {
        view.showSearching();
        final Consumer<PlayerLookupModel> errorAbsentConsumer = playerLookupModel -> {
            view.showPlayerDetails(playerLookupModel);
            calculateScore();
        };
        final Consumer<Throwable> errorConsumer = exception -> view.showError(exception);

        RaiderIOApi.getInstance()
                .searchPlayerAsync(realmProperty.get(), nameProperty.get())
                .thenApply(player -> {
                    player.ifException(errorConsumer).orElse(errorAbsentConsumer);
                    return player;
                });
    }

    private void calculateScore() {
        // This method is called when the player is found and the view is updated
    }

}
