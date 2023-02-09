package de.haevn.controller;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.enumeration.FactionEnum;
import de.haevn.model.seasonal.Faction;
import de.haevn.model.seasonal.KeystoneAchievement;
import de.haevn.model.seasonal.Percentile;
import de.haevn.model.seasonal.SeasonCutoff;
import de.haevn.model.weekly.WeeklyAffix;
import de.haevn.ui.views.CurrentWeekView;
import de.haevn.utils.MathUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CurrentWeekController {
    private final ObjectProperty<FactionEnum> selectedFactionProperty = new SimpleObjectProperty<>(FactionEnum.ALL);
    private final CurrentWeekView view;

    public CurrentWeekController(CurrentWeekView view) {
        this.view = view;

        GitHubApi.getInstance().getAffixRotation().addListener(e -> onWeeklyAffixChanged());
        RaiderIOApi.getInstance().getCurrentCutoff().addListener(e -> onCurrentCutOffChanged());


        selectedFactionProperty.bind(view.getSelectedFaction());
        selectedFactionProperty.addListener(e -> onCurrentCutOffChanged());
    }


    private void onWeeklyAffixChanged() {
        view.setCurrentAffix(WeeklyAffix.getInstance().getCurrentAffixAsString());
        view.setNextAffix(WeeklyAffix.getInstance().getNextAffixAsString());
    }

    private void onCurrentCutOffChanged() {

        SeasonCutoff cutoff = RaiderIOApi.getInstance().getCurrentCutoff().get();
        var selectedFaction = selectedFactionProperty.get();
        view.setKSH(MathUtils.getDecimalFormat().format(getAchievement(selectedFaction, cutoff.getKeystoneHero()).getQuantileMinValue()),
                MathUtils.getDecimalFormat().format(getAchievement(selectedFaction, cutoff.getKeystoneHero()).getQuantilePopulationCount()));
        view.setKSM(MathUtils.getDecimalFormat().format(getAchievement(selectedFaction, cutoff.getKeystoneMaster()).getQuantileMinValue()),
                MathUtils.getDecimalFormat().format(getAchievement(selectedFaction, cutoff.getKeystoneMaster()).getQuantilePopulationCount()));

        view.setTop001(Double.toString(getPercentile(selectedFaction, cutoff.getP999()).getQuantileMinValue()),
                Double.toString(getPercentile(selectedFaction, cutoff.getP999()).getQuantilePopulationCount()));
        view.setTop1(Double.toString(getPercentile(selectedFaction, cutoff.getP990()).getQuantileMinValue()),
                Double.toString(getPercentile(selectedFaction, cutoff.getP990()).getQuantilePopulationCount()));
        view.setTop10(Double.toString(getPercentile(selectedFaction, cutoff.getP900()).getQuantileMinValue()),
                Double.toString(getPercentile(selectedFaction, cutoff.getP900()).getQuantilePopulationCount()));
    }


    private Faction getPercentile(FactionEnum faction, Percentile percentile) {
        return switch (faction) {
            case ALL -> percentile.getAll();
            case ALLIANCE -> percentile.getAlliance();
            case HORDE -> percentile.getHorde();
            case NONE -> new Faction();
        };
    }

    private Faction getAchievement(FactionEnum faction, KeystoneAchievement achievement) {
        return switch (faction) {
            case ALL -> achievement.getAll();
            case ALLIANCE -> achievement.getAlliance();
            case HORDE -> achievement.getHorde();
            case NONE -> new Faction();
        };
    }
}
