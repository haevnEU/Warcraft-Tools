package de.haevn.v1.ui.utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxGroup {
    private final List<CheckBox> checkBoxList = new ArrayList<>();
    private final SimpleBooleanProperty property = new SimpleBooleanProperty();

    public CheckBoxGroup(CheckBox... checkBoxes) {
        addCheckBox(checkBoxes);
    }

    public CheckBoxGroup(List<CheckBox> checkBoxes) {
        addCheckBox(checkBoxes);
    }

    public void addCheckBox(CheckBox... checkBoxes) {
        addCheckBox(List.of(checkBoxes));
    }

    public void addCheckBox(List<CheckBox> checkBoxes) {
        checkBoxList.addAll(checkBoxes);
        checkBoxes.forEach(checkBox -> checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> onSelectionChanged()));
    }

    public List<CheckBox> getSelected() {
        return checkBoxList.stream().filter(CheckBox::isSelected).toList();
    }

    public List<Object> getSelectedUserData() {
        return getSelected().stream().map(Node::getUserData).toList();
    }

    private void onSelectionChanged() {
        property.set(!property.get());
    }

    public void bind(ChangeListener<Boolean> event) {
        property.addListener(event);
    }

    public void reset() {
        checkBoxList.forEach(checkBox -> checkBox.setSelected(false));
    }
}
