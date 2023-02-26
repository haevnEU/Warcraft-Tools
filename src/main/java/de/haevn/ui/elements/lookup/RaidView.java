package de.haevn.ui.elements.lookup;

import de.haevn.model.lookup.RaidProgression;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H2;
import de.haevn.ui.elements.html.H3;
import de.haevn.utils.MathUtils;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class RaidView extends BorderPane {
    private final ListView<RaidProgression.Raid> lvRaids = new ListView<>();
    private final H2 lbRaid = new H2("Raid");
    private final H3 lbCleared = new H3("");
    private final Label lbNormal = new Label();
    private final Label lbHeroic = new Label();
    private final Label lbMythic = new Label();

    public RaidView(String title) {
        setLeft(lvRaids);
        setTop(new H1(title));
        lvRaids.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onDungeonChanged(newValue));

        final GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        final HBox box = new HBox(lbRaid, lbCleared);
        box.setSpacing(10);
        form.add(box, 0, 0, 2, 1);
        form.add(new Label("Normal"), 0, 1);
        form.add(new Label("Heroic"), 0, 2);
        form.add(new Label("Mythic"), 0, 3);

        form.add(lbNormal, 1, 1);
        form.add(lbHeroic, 1, 2);
        form.add(lbMythic, 1, 3);


        setCenter(form);
    }

    private void onDungeonChanged(RaidProgression.Raid dungeon) {
        String amountBosse = MathUtils.numberToRoundText(dungeon.getTotalBosses());
        lbRaid.setText(dungeon.toString());
        lbCleared.setText(dungeon.getSummary());
        lbNormal.setText(dungeon.getNormalBossesKilled() + "/" + amountBosse);
        lbHeroic.setText(dungeon.getHeroicBossesKilled() + "/" + amountBosse);
        lbMythic.setText(dungeon.getMythicBossesKilled() + "/" + amountBosse);
    }


    public void setLvRaids(List<RaidProgression.Raid> dungeons) {
        lvRaids.getItems().clear();
        lvRaids.getItems().addAll(dungeons);
    }
}
