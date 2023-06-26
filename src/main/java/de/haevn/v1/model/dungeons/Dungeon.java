package de.haevn.v1.model.dungeons;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    private final SimpleObjectProperty<List<Enemy>> enemies = new SimpleObjectProperty<>();
    private final String name;
    private final String shortName;

    public Dungeon(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
        enemies.set(new ArrayList<>());
    }

    @Override
    public String toString() {
        return name + " (" + shortName + ")";
    }

    public ReadOnlyObjectProperty<List<Enemy>> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies.set(enemies);
    }
}
