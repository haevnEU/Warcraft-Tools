package de.haevn.ui.widgets.dungeon;

import de.haevn.abstraction.IView;
import de.haevn.model.dungeons.Dungeon;
import de.haevn.model.dungeons.Enemy;
import de.haevn.ui.elements.dungeon.EnemyWidget;
import de.haevn.ui.elements.html.H1;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class DungeonView extends BorderPane implements IView {
    private final ListView<Enemy> enemies = new ListView<>();
    private final ComboBox<Dungeon> dungeonsComboBox = new ComboBox<>();

    private final EnemyWidget enemyWidget = new EnemyWidget();

    DungeonView() {
        setTop(new H1("Dungeon Enemies (WIP)"));
        setLeft(new VBox(dungeonsComboBox, enemies));
        setCenter(enemyWidget);

        enemies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            internalShowEnemy(newValue);
        });
    }

    public void bindDungeons(ObservableList<Dungeon> dungeons) {
        dungeonsComboBox.setItems(dungeons);
    }

    public void setOnDungeonChanged(ChangeListener<Dungeon> event) {
        dungeonsComboBox.getSelectionModel().selectedItemProperty().addListener(event);
    }

    public void setOnEnemyChanged(ChangeListener<Enemy> event) {
        enemies.getSelectionModel().selectedItemProperty().addListener(event);
    }

    public void setEnemies(ObservableList<Enemy> enemies) {
        this.enemies.setItems(enemies);
    }

    private void internalShowEnemy(Enemy enemy) {
        enemyWidget.setEnemy(enemy);
    }
}
