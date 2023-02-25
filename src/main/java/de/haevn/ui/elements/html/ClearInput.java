package de.haevn.ui.elements.html;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class ClearInput extends GridPane {
    private final List<EventHandler<ActionEvent>> onActionEventHandler = new ArrayList<>();

    private final TextField textField = new TextField();

    public ClearInput() {
        this("");
    }

    public ClearInput(String text) {
        this(text, false);
    }

    public ClearInput(String text, boolean readonly) {
        setText(text);
        textField.setEditable(readonly);
        textField.setOnMouseClicked(event1 -> textField.selectAll());

        addOnClearClicked(event -> textField.setText(""));
        Button btClear = new Button("Clear");
        btClear.setOnAction(event -> onActionEventHandler.forEach(actionEventEventHandler -> actionEventEventHandler.handle(event)));
        Button btCopy = new Button("Copy");
        btCopy.setOnAction(event -> {
            textField.selectAll();
            textField.copy();
        });
        add(textField, 0, 0);
        add(btClear, 1, 0);
        add(btCopy, 2, 0);

        setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();

        col1.setPercentWidth(90);
        col2.setPercentWidth(7);
        col3.setPercentWidth(7);
        getColumnConstraints().addAll(col1, col2, col3);
    }

    public void addOnClearClicked(EventHandler<ActionEvent> event) {
        onActionEventHandler.add(event);
    }

    public void setOnTextChangeAction(ChangeListener<String> event) {
        textField.textProperty().addListener(event);
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public String getText() {
        return textField.getText();
    }

    public StringProperty getTextProperty() {
        return textField.textProperty();
    }

    public void bindTextProperty(StringProperty property) {
        textField.textProperty().bindBidirectional(property);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }


}
