package de.haevn.ui.widgets.currentweek;

import de.haevn.abstraction.IController;
import de.haevn.abstraction.IModel;
import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.enumeration.FactionEnum;
import de.haevn.model.seasonal.Faction;
import de.haevn.model.seasonal.KeystoneAchievement;
import de.haevn.model.seasonal.Percentile;
import de.haevn.model.seasonal.SeasonCutoff;
import de.haevn.model.weekly.Affix;
import de.haevn.model.weekly.WeeklyAffix;
import de.haevn.utils.MathUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class CurrentWeekController implements IController {
    private final ObjectProperty<FactionEnum> selectedFactionProperty = new SimpleObjectProperty<>(FactionEnum.ALL);
    private CurrentWeekView view;

    CurrentWeekController() {
        RaiderIOApi.getInstance().getCurrentAffix().addListener((observable, oldValue, newValue) -> updateAffixes());
        GitHubApi.getInstance().getAffixRotation().addListener((observable, oldValue, newValue) -> updateAffixes());
    }

    private void updateAffixes() {
        var currentRotation = WeeklyAffix.getInstance().getCurrentAffix().get();
        final String currentAffix = String.join(", ", currentRotation.stream().map(Affix::getName).toArray(String[]::new));
        view.setCurrentAffix(currentAffix);

        var nextRotation = WeeklyAffix.getInstance().getNextWeekRotation();
        final String nextAffix = String.join(", ", nextRotation.stream().map(Affix::getName).toArray(String[]::new));
        view.setNextAffix(nextAffix);
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

    @Override
    public void link(IView view, IModel model) {
        if (!(view instanceof CurrentWeekView))
            throw new IllegalArgumentException("View must be of type CurrentWeekView!");

        this.view = (CurrentWeekView) view;

        RaiderIOApi.getInstance().getCurrentCutoff().addListener(e -> onCurrentCutOffChanged());


        selectedFactionProperty.bind(this.view.getSelectedFaction());
        selectedFactionProperty.addListener(e -> onCurrentCutOffChanged());
    }
}
