package de.haevn.ui.elements.dungeon;

import de.haevn.api.WowHeadApi;
import de.haevn.model.dungeons.Enemy;
import de.haevn.model.weekly.Scaling;
import de.haevn.model.weekly.WeeklyAffix;
import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.AH3;
import de.haevn.ui.elements.html.ErrorLabel;
import de.haevn.utils.MathUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


public class EnemyWidget extends GridPane {
    private final ErrorLabel errorLabel = new ErrorLabel();
    private final AH3 header = new AH3();
    private final Label health = new Label();
    private final Label creatureType = new Label();
    private final Label level = new Label();

    private final Label isBoss = new Label();

    private final TextField tfLevel = new TextField();


    private final FlowPane spellPane = new FlowPane();
    private final FlowPane activeCharacteristics = new FlowPane();
    private final FlowPane inactiveCharacteristics = new FlowPane();
    private Enemy enemy = null;

    public EnemyWidget() {
        final Label lbHealth = new Label("Health: ");
        final Label lbCreature = new Label("Creature Type: ");
        final Label lbLevel = new Label("Level: ");
        final Label lbBoss = new Label("Is Boss: ");
        final Label spells = new Label("Spells: ");
        final Label lbActives = new Label("Active");
        final Label lbInactives = new Label("Inactive");

        GridPane.setValignment(lbHealth, VPos.TOP);
        GridPane.setValignment(lbCreature, VPos.TOP);
        GridPane.setValignment(lbLevel, VPos.TOP);
        GridPane.setValignment(lbBoss, VPos.TOP);
        GridPane.setValignment(spells, VPos.TOP);
        GridPane.setValignment(lbActives, VPos.TOP);
        GridPane.setValignment(lbInactives, VPos.TOP);


        add(header, 0, 0, 2, 1);
        add(lbHealth, 0, 1);
        add(health, 1, 1);
        add(lbCreature, 0, 2);
        add(creatureType, 1, 2);
        add(lbLevel, 0, 3);
        add(level, 1, 3);
        add(lbBoss, 0, 4);
        add(isBoss, 1, 4);

        add(new Label("Level: "), 0, 5);
        add(tfLevel, 1, 5);

        add(spells, 0, 8);
        add(spellPane, 1, 8);

        add(lbActives, 0, 9);
        add(activeCharacteristics, 1, 9);
        add(lbInactives, 0, 10);
        add(inactiveCharacteristics, 1, 10);

        tfLevel.textProperty().addListener((observable, old, value) -> keystoneLevelChanged(value));

        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        getColumnConstraints().addAll(column1, column2);
    }

    private void keystoneLevelChanged(String value) {
        if (null == enemy) {
            errorLabel.setText("No enemy selected");
            errorLabel.setVisible(true);
            return;
        }
        final int keystoneLevel = MathUtils.stringToInteger(value);
        health.setText(MathUtils.numberToShortValue(multiplyHealth(keystoneLevel)));

    }

    private double multiplyHealth(int keystoneLevel) {
        double result = enemy.getHealth();
        double multiplier = 1.2;
        if (enemy.isBoss() && WeeklyAffix.isTyrannical()) {
            multiplier = 1.3;
        }

        result *= Scaling.getInstance().getScalarFor(keystoneLevel);
        result *= multiplier;
        result = Math.round(result);
        return result;
    }

    public void setEnemy(Enemy enemy) {
        if (null == enemy) {
            errorLabel.setText("No enemy selected");
            errorLabel.setVisible(true);
            return;
        }

        final int keystoneLevel = MathUtils.stringToInteger(tfLevel.getText());
        this.enemy = enemy;

        errorLabel.setVisible(false);
        header.setText(enemy.getName() + " (" + enemy.getId() + ")");
        header.setLink("https://www.wowhead.com/de/npc=" + enemy.getId());
        health.setText(MathUtils.numberToShortValue(multiplyHealth(keystoneLevel)));
        creatureType.setText(enemy.getCreatureType());
        level.setText(String.valueOf(enemy.getLevel()));

        spellPane.getChildren().clear();
        enemy.getSpells().forEach(spell ->
                WowHeadApi.getInstance().getNameForSpell(spell).thenApply(name -> {
                    Platform.runLater(() -> {
                        if (!name.isEmpty() && !name.contains("error occurred")) {
                            final A label = new A();
                            label.setText(name);
                            label.setLink("https://www.wowhead.com/spell=" + spell);
                            spellPane.getChildren().add(label);
                        }
                    });
                    return null;
                })
        );

        isBoss.setText(String.valueOf(enemy.isBoss()));


        activeCharacteristics.getChildren().clear();


        Platform.runLater(() -> {
            activeCharacteristics.getChildren().clear();
            enemy.getCharacteristics().getActive().forEach(characteristic -> {
                final Label label = new Label(characteristic);
                activeCharacteristics.getChildren().add(label);
            });
            if (enemy.isStealthDetect()) {
                final Label label = new Label("stealth detect");
                activeCharacteristics.getChildren().add(label);
            }
        });


        Platform.runLater(() -> {
            inactiveCharacteristics.getChildren().clear();
            enemy.getCharacteristics().getInactive().forEach(characteristic -> {
                final Label label = new Label(characteristic);
                inactiveCharacteristics.getChildren().add(label);
            });
            if (!enemy.isStealthDetect()) {
                final Label label = new Label("stealth detect");
                inactiveCharacteristics.getChildren().add(label);
            }
        });

    }
}
