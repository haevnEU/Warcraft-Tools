package de.haevn.ui.views;

import de.haevn.abstraction.IView;
import de.haevn.enumeration.FactionEnum;
import de.haevn.ui.widgets.GroupWidget;
import de.haevn.ui.widgets.currentweek.CutoffWidget;
import de.haevn.ui.widgets.html.H1;
import de.haevn.ui.widgets.html.H2;
import de.haevn.ui.widgets.html.H3;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;

public class CurrentWeekView extends BorderPane implements IView {
    private final H3 lbCurrentAffix = new H3();
    private final H3 lbNextAffix = new H3();
    private final CutoffWidget top001Widget = new CutoffWidget();
    private final CutoffWidget top1Widget = new CutoffWidget();
    private final CutoffWidget top10Widget = new CutoffWidget();
    private final CutoffWidget ksmWidget = new CutoffWidget();
    private final CutoffWidget kshWidget = new CutoffWidget();
    private final ComboBox<FactionEnum> cbFaction = new ComboBox<>(FXCollections.observableArrayList(FactionEnum.ALL, FactionEnum.ALLIANCE, FactionEnum.HORDE));

    public CurrentWeekView() {
        cbFaction.getSelectionModel().select(FactionEnum.ALL);
        final GridPane pane = new GridPane();
        final FlowPane widgetCutoff = new FlowPane();
        final FlowPane widgetAchievements = new FlowPane();

        widgetCutoff.setId("widget-box");
        widgetAchievements.setId("widget-box");

        widgetCutoff.getChildren().addAll(
                new GroupWidget("Top 0.01%", top001Widget),
                new GroupWidget("Top 1%", top1Widget),
                new GroupWidget("Top 10%", top10Widget));
        widgetAchievements.getChildren().addAll(
                new GroupWidget("Keystone Hero", kshWidget),
                new GroupWidget("Keystone Master", ksmWidget));
        pane.add(new H2("Current week"), 0, 0);
        pane.add(new H2("Next week"), 0, 1);

        pane.add(lbCurrentAffix, 1, 0);
        pane.add(lbNextAffix, 1, 1, 2, 1);
        pane.add(new HBox(new H3("Faction"), cbFaction), 0, 2, 2, 1);
        pane.add(widgetCutoff, 0, 3, 2, 1);
        pane.add(widgetAchievements, 0, 4, 2, 1);
        pane.setMaxWidth(Integer.MAX_VALUE);
        pane.setHgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);

        pane.getColumnConstraints().addAll(col1, col2);
        setCenter(pane);
        setTop(new H1("Current Week"));
    }

    public void setCurrentAffix(String affix) {
        lbCurrentAffix.setText(affix);
    }

    public void setNextAffix(String affix) {
        lbNextAffix.setText(affix);
    }

    public void setTop001(String score, String achieved) {
        top001Widget.setData(score, achieved);
    }

    public void setTop1(String score, String achieved) {
        top1Widget.setData(score, achieved);
    }

    public void setTop10(String score, String achieved) {
        top10Widget.setData(score, achieved);
    }

    public void setKSH(String score, String achieved) {
        kshWidget.setData(score, achieved);
    }

    public void setKSM(String score, String achieved) {
        ksmWidget.setData(score, achieved);
    }

    public ReadOnlyObjectProperty<FactionEnum> getSelectedFaction() {
        return cbFaction.valueProperty();
    }

}
