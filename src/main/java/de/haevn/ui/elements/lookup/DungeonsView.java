package de.haevn.ui.elements.lookup;

import de.haevn.model.lookup.MythicPlusDungeon;
import de.haevn.model.weekly.WeeklyAffix;
import de.haevn.ui.elements.html.AH2;
import de.haevn.ui.elements.html.H1;
import de.haevn.utils.MathUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class DungeonsView extends BorderPane {
    private final ListView<MythicPlusDungeon> lvDungeons = new ListView<>();
    private final AH2 lbDungeon = new AH2("Dungeon", "");
    private final Label lbLevel = new Label();
    private final Label lbCompleted = new Label();
    private final Label lbTimeRemain = new Label();
    private final Label lbTime = new Label();
    private final Label lbParTime = new Label();
    private final Label lbUpgrade = new Label();
    private final Label lbScore = new Label();
    private final Label lbAffix = new Label();

    public DungeonsView(String title) {
        setLeft(lvDungeons);
        setTop(new H1(title));
        lvDungeons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onDungeonChanged(newValue));

        final GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(lbDungeon, 0, 0, 2, 1);
        form.add(new Label("Level"), 0, 1);
        form.add(new Label("Completed"), 0, 2);
        form.add(new Label("Time remaining"), 0, 3);
        form.add(new Label("Time"), 0, 4);
        form.add(new Label("ParTime"), 0, 5);
        form.add(new Label("Upgrades"), 0, 6);
        form.add(new Label("Score"), 0, 7);
        form.add(new Label("Affix"), 0, 8);

        form.add(lbLevel, 1, 1);
        form.add(lbCompleted, 1, 2);
        form.add(lbTimeRemain, 1, 3);
        form.add(lbTime, 1, 4);
        form.add(lbParTime, 1, 5);
        form.add(lbUpgrade, 1, 6);
        form.add(lbScore, 1, 7);
        form.add(lbAffix, 1, 8);


        setCenter(form);
    }

    private void onDungeonChanged(MythicPlusDungeon dungeon) {
        String title = dungeon.getDungeon() + " (" + (dungeon.getNumKeystoneUpgrades() > 0 ? "Timed" : "Untimed") + ")";
        lbDungeon.setText(title);
        lbDungeon.setLink(dungeon.getUrl());
        lbLevel.setText(MathUtils.numberToRoundText(dungeon.getMythicLevel()));
        lbCompleted.setText(dungeon.getCompletedAt().toString());
        lbTimeRemain.setText(MathUtils.msToString(dungeon.getParTimeMs() - dungeon.getClearTimeMs()));
        lbTime.setText(MathUtils.msToString(dungeon.getClearTimeMs()));
        lbParTime.setText(MathUtils.msToString(dungeon.getParTimeMs()));
        lbUpgrade.setText(MathUtils.numberToRoundText(dungeon.getNumKeystoneUpgrades()));
        lbScore.setText(MathUtils.numberToRoundText(dungeon.getScore()));
        lbAffix.setText(WeeklyAffix.convertToString(dungeon.getAffixes()));
    }


    public void setDungeons(List<MythicPlusDungeon> dungeons) {
        lvDungeons.getItems().clear();
        lvDungeons.getItems().addAll(dungeons);
    }
}
