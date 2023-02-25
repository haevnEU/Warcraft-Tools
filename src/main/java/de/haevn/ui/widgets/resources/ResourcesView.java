package de.haevn.ui.widgets.resources;

import de.haevn.abstraction.IView;
import de.haevn.ui.utils.Creator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
