package de.haevn.ui.widgets.dungeon;

import com.fasterxml.jackson.databind.JsonNode;
import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.model.dungeons.Dungeon;
import de.haevn.model.dungeons.Dungeons;
import de.haevn.model.dungeons.Enemy;
import de.haevn.utils.FileIO;
import de.haevn.utils.SerializationUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class DungeonController implements IController {
    private final SimpleObjectProperty<Dungeons> seasonOneDungeons = new SimpleObjectProperty<>(new Dungeons());
    private DungeonView view;

    public SimpleObjectProperty<Dungeons> getSeasonOneDungeons() {
        return seasonOneDungeons;
    }

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof DungeonView)) {
            throw new IllegalArgumentException("View is not of type DungeonView");
        }
        this.view = (DungeonView) view;

        reload();
        this.view.bindDungeons(FXCollections.observableArrayList(seasonOneDungeons.get().getDungeons()));
        this.view.setOnDungeonChanged((e, old, value) -> dungeonSelectionChanged(value));
    }

    private void dungeonSelectionChanged(Dungeon value) {
        var enemies = FXCollections.observableArrayList(value.getEnemies().get());
        view.setEnemies(enemies);
    }

    public void reload() {
        try {
            final List<String> data = Files.readAllLines(Path.of(FileIO.getRootPathWithSeparator() + "json/dungeons_df_1.json"));
            final String json = String.join("\n", data);
            final JsonNode root = SerializationUtils.parseJson(json, JsonNode.class);
            final Dungeons dungeons = new Dungeons();

            final Function<JsonNode, ArrayList<Enemy>> extractEnemies = (JsonNode node) -> {
                ArrayList<Enemy> enemies = new ArrayList<>();
                for (JsonNode enemyNode : node) {
                    var enemy = SerializationUtils.parseJsonSecure(enemyNode.toString(), Enemy.class);
                    enemy.ifPresent(enemies::add);
                }
                return enemies;
            };

            dungeons.getAlgetharAcademy().setEnemies(extractEnemies.apply(root.get("AA")));
            dungeons.getAzureVaults().setEnemies(extractEnemies.apply(root.get("AV")));
            dungeons.getNokudOffensive().setEnemies(extractEnemies.apply(root.get("NO")));
            dungeons.getRubyLifePools().setEnemies(extractEnemies.apply(root.get("RLP")));
            dungeons.getCourtOfStarts().setEnemies(extractEnemies.apply(root.get("COS")));
            dungeons.getHallsOfValor().setEnemies(extractEnemies.apply(root.get("HOV")));
            dungeons.getTempleOfJadeSerpent().setEnemies(extractEnemies.apply(root.get("TJS")));
            dungeons.getShadowmoonBurialGround().setEnemies(extractEnemies.apply(root.get("SBG")));

            seasonOneDungeons.set(dungeons);

        } catch (Exception ex) {
            seasonOneDungeons.set(new Dungeons());
        }
    }
}
