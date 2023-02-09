package de.haevn.ui.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Creator {
    private Creator(){}

    public static Button createButton(String title, EventHandler<ActionEvent> event){
        Button button = new Button(title);
        button.setOnAction(event);
        return button;
    }

    public static Tab createTab(String title, Node node){
        Tab tab = new Tab(title);
        tab.setContent(node);
        tab.setClosable(false);
        return tab;
    }

    public static GridPane createForm(Map<String, Node> entries){
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        AtomicInteger row = new AtomicInteger();
        entries.forEach((text, node) -> {
            pane.add(new Label(text), 0, row.get());
            pane.add(node, 1, row.get());
            row.getAndIncrement();
        });

        return pane;
    }

    public static TitledPane generateTitledPane(String title, Node content, boolean expanded) {
        TitledPane tp = new TitledPane(title, content);
        tp.getStyleClass().add("fx-haevn-titledpane");
        tp.setExpanded(expanded);
        return tp;
    }
}
