package de.haevn.ui.elements.lookup;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchBox extends GridPane {
    private final List<EventHandler<ActionEvent>> onActionEventHandler = new ArrayList<>();
    private final TextField searchField = new TextField();
    private final TextField nameBox = new TextField();
    private final TextField realmBox = new TextField();


    public SearchBox() {

        add(new Label("Raider.io link"), 0, 0);
        add(searchField, 1, 0, 3, 1);

        add(new Label("Name"), 0, 1);
        add(nameBox, 1, 1);
        add(new Label("Realm"), 2, 1);
        add(realmBox, 3, 1);

        Button btSearch = new Button("Search");
        add(btSearch, 1, 2, 3, 1);

        GridPane.setHgrow(searchField, Priority.ALWAYS);
        GridPane.setHgrow(nameBox, Priority.ALWAYS);
        GridPane.setHgrow(realmBox, Priority.ALWAYS);
        GridPane.setHgrow(btSearch, Priority.ALWAYS);
        btSearch.setMaxWidth(Double.MAX_VALUE);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchBoxUpdate());
        btSearch.setOnAction(this::onSearchClicked);
    }

    private void onSearchClicked(ActionEvent event) {
        onActionEventHandler.forEach(actionEventEventHandler -> actionEventEventHandler.handle(event));
    }

    public void addOnSearchClicked(EventHandler<ActionEvent> event) {
        onActionEventHandler.add(event);
    }

    public void bindTextFieldNameContent(StringProperty property) {
        nameBox.textProperty().bindBidirectional(property);
    }

    public void bindTextFieldRealmContent(StringProperty property) {
        realmBox.textProperty().bindBidirectional(property);
    }

    public StringProperty getRealm() {
        return realmBox.textProperty();
    }

    public String getRealmString() {
        return realmBox.textProperty().getValue();
    }

    public StringProperty getName() {
        return nameBox.textProperty();
    }

    public String getNameString() {
        return nameBox.textProperty().getValue();
    }

    private void searchBoxUpdate() {
        if (searchField.getText().isBlank()) {
            return;
        }

        String q = searchField.getText();
        if (q.contains("?")) {
            q = StringUtils.split(q, "?")[0];
        }
        var splitQuery = q.split("/");
        int size = splitQuery.length;
        var realm = size >= 2 ? splitQuery[splitQuery.length - 2] : "";
        var character = size >= 1 ? splitQuery[splitQuery.length - 1] : "";
        nameBox.setText(character);
        realmBox.setText(realm);
    }
}
