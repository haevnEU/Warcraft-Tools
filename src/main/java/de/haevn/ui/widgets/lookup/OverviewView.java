package de.haevn.ui.widgets.lookup;

import de.haevn.enumeration.Season;
import de.haevn.model.lookup.MythicPlusDungeon;
import de.haevn.model.lookup.MythicPlusScore;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.widgets.html.H2;
import de.haevn.utils.MathUtils;
import de.haevn.utils.ScoreUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.function.Predicate;

public class OverviewView extends ScrollPane {
    private final SimpleObjectProperty<Average> bestRuns = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Average> highestRuns = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Average> recentRuns = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Statistic> statistic = new SimpleObjectProperty<>();

    public OverviewView() {
        setStyle("-fx-background: transparent; -fx-background-color: transparent; ");
        setPadding(new Insets(10));
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        GridPane root = new GridPane();
        root.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");

        root.add(createSummaryBox(bestRuns, "Best Runs"), 0, 0);
        root.add(createSummaryBox(highestRuns, "Highest Runs"), 1, 0);
        root.add(createSummaryBox(recentRuns, "Recent Runs"), 2, 0);
        root.add(createStatisticBox(statistic), 3, 0);

        root.setHgap(10);

        setOnScroll(this::onScroll);

        setContent(root);
    }

    private void onScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            setHvalue(getHvalue() + 0.33);
        } else {
            setHvalue(getHvalue() - 0.33);
        }
    }

    public void setPlayer(PlayerLookupModel player) {
        bestRuns.set(calculateAverage(player.getMythicPlusBestRuns()));
        highestRuns.set(calculateAverage(player.getMythicPlusHighestLevelRuns()));
        recentRuns.set(calculateAverage(player.getMythicPlusRecentRuns()));


        double current = player.getMythicPlusScoresBySeason()
                .stream()
                .filter(getFilter(Season.getCurrentSeasonKey()))
                .findFirst()
                .orElseGet(MythicPlusScore::new)
                .getAll();
        double prev = player.getMythicPlusScoresBySeason()
                .stream().filter(getFilter(Season.getPreviousSeasonKey()))
                .findFirst()
                .orElseGet(MythicPlusScore::new)
                .getAll();
        statistic.set(new Statistic(current, prev));

    }

    private Predicate<MythicPlusScore> getFilter(Season season) {
        return s -> s.getSeason().equalsIgnoreCase(season.label);
    }

    private Average calculateAverage(List<MythicPlusDungeon> dungeons) {
        double level = dungeons.stream().mapToDouble(MythicPlusDungeon::getMythicLevel).average().orElse(0);
        double upgrade = dungeons.stream().mapToDouble(MythicPlusDungeon::getNumKeystoneUpgrades).average().orElse(0);
        double clearTime = dungeons.stream().mapToDouble(MythicPlusDungeon::getClearTimeMs).average().orElse(0);
        double averageTimeRemain = dungeons.stream().mapToDouble(value -> value.getParTimeMs() - value.getClearTimeMs()).average().orElse(0);
        double amountFortified = dungeons.stream().filter(MythicPlusDungeon::isFortified).count();
        double amountTyrannical = dungeons.stream().filter(MythicPlusDungeon::isTyrannical).count();
        double timed = dungeons.stream().filter(dungeon -> dungeon.getNumKeystoneUpgrades() > 0).count();
        double depleates = dungeons.size() - timed;
        String tendency = dungeons.stream().mapToDouble(MythicPlusDungeon::getNumKeystoneUpgrades).average().orElse(0) > 1 ? "Timed ▲" : "Untimed ▼";
        return new Average(level, upgrade, clearTime, averageTimeRemain, amountFortified, amountTyrannical, timed, depleates, tendency);
    }


    private GridPane createSummaryBox(SimpleObjectProperty<Average> data, String title) {
        GridPane grid = new GridPane();
        grid.setMinWidth(200);
        grid.setMaxWidth(200);
        grid.setId("summary-box");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new H2(title), 0, 0, 2, 1);
        grid.add(new Label("Level"), 0, 1);
        grid.add(new Label("Clear time"), 0, 2);
        grid.add(new Label("Upgrades"), 0, 3);
        grid.add(new Label("Timed"), 0, 4);
        grid.add(new Label("Depleates"), 0, 5);
        grid.add(new Label("Fortified"), 0, 6);
        grid.add(new Label("Tyrannical"), 0, 7);
        grid.add(new Label("Tendency"), 0, 8);

        Label lblLevel = new Label();
        lblLevel.textProperty().bind(data.map(average -> String.valueOf(average.level())));

        final Label lblClearTime = new Label();
        lblClearTime.textProperty().bind(data.map(average -> MathUtils.msToString(average.clearTime())));

        final Label lblUpgrades = new Label();
        lblUpgrades.textProperty().bind(data.map(average -> String.valueOf(average.upgrade())));

        final Label lblDepletes = new Label();
        lblDepletes.textProperty().bind(data.map(average -> String.valueOf(average.depleates())));

        final Label lblFortified = new Label();
        lblFortified.textProperty().bind(data.map(average -> String.valueOf(average.amountFortified())));

        final Label lblTyrannical = new Label();
        lblTyrannical.textProperty().bind(data.map(average -> String.valueOf(average.amountTyrannical())));

        final Label lblTendency = new Label();
        lblTendency.textProperty().bind(data.map(Average::tendency));

        final Label lbTimed = new Label();
        lbTimed.textProperty().bind(data.map(average -> String.valueOf(average.timed())));

        grid.add(lblClearTime, 1, 2);
        grid.add(lblUpgrades, 1, 3);
        grid.add(lbTimed, 1, 4);
        grid.add(lblDepletes, 1, 5);
        grid.add(lblFortified, 1, 6);
        grid.add(lblTyrannical, 1, 7);
        grid.add(lblTendency, 1, 8);


        grid.add(lblLevel, 1, 1);
        return grid;
    }

    private GridPane createStatisticBox(SimpleObjectProperty<Statistic> data) {
        GridPane grid = new GridPane();
        grid.setMinWidth(200);
        grid.setMaxWidth(200);
        grid.setId("summary-box");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new H2("Statistic"), 0, 0, 2, 1);
        grid.add(new Label("Current season"), 0, 1);
        grid.add(new Label("Previous season"), 0, 3);

        final Label lbCurrentScore = new Label();
        lbCurrentScore.textProperty().bind(data.map(value -> String.valueOf(value.currentSeason())));

        final Label lblCurrentCutoff = new Label();
        lblCurrentCutoff.textProperty().bind(data.map(value -> ScoreUtils.getInstance().getPercentileForScore(value.currentSeason(), Season.getCurrentSeasonKey())));

        final Label lbPreviousScore = new Label();
        lbPreviousScore.textProperty().bind(data.map(value -> String.valueOf(value.previousSeason())));

        final Label lbPreviousCutoff = new Label();
        lbPreviousCutoff.textProperty().bind(data.map(value -> ScoreUtils.getInstance().getPercentileForScore(value.previousSeason(), Season.getPreviousSeasonKey())));


        grid.add(lbCurrentScore, 1, 1);
        grid.add(lblCurrentCutoff, 1, 2);
        grid.add(lbPreviousScore, 1, 3);
        grid.add(lbPreviousCutoff, 1, 4);
        return grid;
    }


    private record Statistic(double currentSeason, double previousSeason) {
    }

    private record Average(
            double level,
            double upgrade,
            double clearTime,
            double averageTimeRemain,
            double amountFortified,
            double amountTyrannical,
            double timed,
            double depleates,
            String tendency
    ) {
    }
}
