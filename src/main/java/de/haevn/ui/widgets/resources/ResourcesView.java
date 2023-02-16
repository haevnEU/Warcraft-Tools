package de.haevn.ui.widgets.resources;

import de.haevn.abstraction.IView;
import de.haevn.exceptions.NetworkException;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.elements.ErrorWidget;
import de.haevn.ui.elements.ProgressWidget;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H3;
import de.haevn.ui.elements.lookup.*;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.Network;
import de.haevn.utils.PropertyHandler;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

class ResourcesView extends BorderPane implements IView {

    final TabPane tabs = new TabPane();
    public ResourcesView() {


        setPadding(new Insets(10, 10, 10, 10));
        setCenter(tabs);


        final HBox controlBox = new HBox();
        controlBox.setSpacing(10);
        controlBox.getChildren().add(Creator.createButton("Back", 100, event -> goback()));
        controlBox.getChildren().add(Creator.createButton("Forward", 100, event -> goforward()));
        controlBox.getChildren().add(Creator.createButton("Reload", 100, event -> reload()));
        setTop(controlBox);
    }


    private void goback(){
        if(tabs.getSelectionModel().selectedItemProperty().get().getContent() instanceof WebView webView){
            Platform.runLater(() -> webView.getEngine().executeScript("history.back()"));
        }
    }

    private void goforward(){
        if(tabs.getSelectionModel().selectedItemProperty().get().getContent() instanceof WebView webView){
            Platform.runLater(() -> webView.getEngine().executeScript("history.forward()"));
        }
    }

    private void reload(){
        if(tabs.getSelectionModel().selectedItemProperty().get().getContent() instanceof WebView webView){
            Platform.runLater(() -> webView.getEngine().reload());
        }
    }


    public void addResource(String name, String url) {
        final Tab tab = new Tab(name);
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load(url);
        tab.setContent(webView);
        tab.setClosable(false);
        tabs.getTabs().add(tab);
    }

}
