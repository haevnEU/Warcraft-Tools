package de.haevn.model.weekly;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.utils.ListOperation;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.ArrayList;
import java.util.List;

public final class WeeklyAffix {
    private static final WeeklyAffix INSTANCE = new WeeklyAffix();

    public static WeeklyAffix getInstance() {
        return INSTANCE;
    }

    public static String convertToString(WeeklyAffix affix) {
        return convertToString(affix.getCurrentAffix().get());
    }

    public static String convertToString(List<Affix> affixes) {
        return String.join(", ", affixes.stream().map(Affix::getName).toArray(String[]::new));
    }

    public static boolean isFortified() {
        return getInstance().getCurrentAffix().get().stream().anyMatch(affix -> affix.getId().equalsIgnoreCase("10"));
    }

    public static boolean isTyrannical() {
        return !isFortified();
    }

    public String getCurrentAffixAsString() {
        if (getCurrentAffix().get() == null) return "Cannot load affixes";
        return String.join(", ", getCurrentAffix().get().stream().map(Affix::getName).toArray(String[]::new));
    }

    public String getNextAffixAsString() {
        return String.join(", ", getNextWeekRotation().stream().map(Affix::getName).toArray(String[]::new));
    }

    public ReadOnlyObjectProperty<List<Affix>> getCurrentAffix() {
        return RaiderIOApi.getInstance().getCurrentAffix();
    }

    public List<Affix> getNextWeekRotation() {
        List<Affix> nextWeekRotation = new ArrayList<>();
        GitHubApi.getInstance().getAffixRotation().get().forEach((s, affixes) -> {
            var t2 = getCurrentAffix().get();
            if (ListOperation.isContentEqual(affixes, t2, Affix.COMPARE)) {
                int id = (Integer.parseInt(s) + 1) % 10;
                nextWeekRotation.clear();
                var t = GitHubApi.getInstance().getAffixRotation().get().get(String.valueOf(id));
                nextWeekRotation.addAll(t);
            }
        });

        return nextWeekRotation;
    }


    public List<Affix> getRotationAfter(List<Affix> currentAffixRotation) {
        List<Affix> nextWeekRotation = new ArrayList<>();
        GitHubApi.getInstance().getAffixRotation().get().forEach((s, affixes) -> {
            if (ListOperation.isContentEqual(affixes, currentAffixRotation, Affix.COMPARE)) {
                int id = (Integer.parseInt(s) + 1) % 10;
                nextWeekRotation.clear();
                var t = GitHubApi.getInstance().getAffixRotation().get().get(String.valueOf(id));
                nextWeekRotation.addAll(t);
            }
        });

        return nextWeekRotation;
    }
}
