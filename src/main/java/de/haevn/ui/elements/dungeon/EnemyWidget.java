package de.haevn.ui.elements.dungeon;

import de.haevn.api.WowHeadApi;
import de.haevn.model.dungeons.Enemy;
import de.haevn.model.weekly.Scaling;
import de.haevn.model.weekly.WeeklyAffix;
import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.AH3;
import de.haevn.ui.elements.html.ErrorLabel;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.MathUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class EnemyWidget extends VBox {
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

        final GridPane generalPane = new GridPane();
        final Label lbHealth = new Label("Health: ");
        final Label lbCreature = new Label("Creature Type: ");
        final Label lbLevel = new Label("Level: ");
        final Label lbBoss = new Label("Is Boss: ");

        generalPane.add(lbHealth, 0, 1);
        generalPane.add(health, 1, 1);
        generalPane.add(lbCreature, 0, 2);
        generalPane.add(creatureType, 1, 2);
        generalPane.add(lbLevel, 0, 3);
        generalPane.add(level, 1, 3);
        generalPane.add(lbBoss, 0, 4);
        generalPane.add(isBoss, 1, 4);

        generalPane.add(new Label("Level: "), 0, 5);
        generalPane.add(tfLevel, 1, 5);

        GridPane.setValignment(lbHealth, VPos.TOP);
        GridPane.setValignment(lbCreature, VPos.TOP);
        GridPane.setValignment(lbLevel, VPos.TOP);
        GridPane.setValignment(lbBoss, VPos.TOP);

        getChildren().add(header);
        getChildren().add(Creator.generateTitledPane("General", generalPane, true));
        getChildren().add(Creator.generateTitledPane("Spells", spellPane, true));
        getChildren().add(Creator.generateTitledPane("Active Characteristics", activeCharacteristics, true));
        getChildren().add(Creator.generateTitledPane("Inactive Characteristics", inactiveCharacteristics, false));

        setSpacing(10);

        tfLevel.textProperty().addListener((observable, old, value) -> keystoneLevelChanged(value));


        setPadding(new Insets(10));
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        generalPane.getColumnConstraints().addAll(column1, column2);
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
        double multiplier = 1;
        if (keystoneLevel > 10) {
            if (enemy.isBoss() && WeeklyAffix.isTyrannical()) {
                multiplier = 1.3;
            } else {
                multiplier = 1.2;
            }
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
