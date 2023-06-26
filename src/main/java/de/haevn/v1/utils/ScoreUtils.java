package de.haevn.v1.utils;

import de.haevn.v1.api.RaiderIOApi;
import de.haevn.v1.enumeration.Addon;
import de.haevn.v1.enumeration.Season;
import de.haevn.v1.model.lookup.MythicPlusScore;
import de.haevn.v1.model.lookup.PlayerLookupModel;
import de.haevn.v1.model.seasonal.SeasonCutoff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class ScoreUtils {
    private static final ScoreUtils INSTANCE = new ScoreUtils();

    private ScoreUtils() {
    }

    public static ScoreUtils getInstance() {
        return INSTANCE;
    }

    public String getPercentileForScore(double score, Season season) {
        final Map<Addon, List<SeasonCutoff>> cutoffs = RaiderIOApi.getInstance().getCutoffs().get();

        final Predicate<SeasonCutoff> filter = cutoff -> cutoff.getSeasonKey().equalsIgnoreCase(season.label);
        final List<SeasonCutoff> cuttofList = new ArrayList<>();

        cutoffs.forEach((addon, seasonCutoffs) -> cuttofList.addAll(seasonCutoffs));

        return cuttofList.stream().filter(filter).findFirst().map(cutoff -> {
            if (cutoff.getP999().getAll().getQuantileMinValue() < score) {
                return "Top 0.1%";
            } else if (cutoff.getP990().getAll().getQuantileMinValue() < score) {
                return "Top 1%";
            } else if (cutoff.getP900().getAll().getQuantileMinValue() < score) {
                return "Top 10%";
            } else if (cutoff.getP750().getAll().getQuantileMinValue() < score) {
                return "Above 25%";
            } else if (cutoff.getP600().getAll().getQuantileMinValue() < score) {
                return "Above 40%";
            }
            return "Below 40%";
        }).orElse("N/A");
    }


    public double getScoreForCurrentSeason(PlayerLookupModel player) {
        return getScoreForSeason(player, Season.getCurrentSeasonKey());
    }

    public double getScoreForPreviousSeason(PlayerLookupModel player) {
        return getScoreForSeason(player, Season.getPreviousSeasonKey());
    }

    public double getScoreForSeason(PlayerLookupModel player, Season season) {
        return player.getMythicPlusScoresBySeason()
                .stream()
                .filter(s -> s.getSeason().equalsIgnoreCase(season.label))
                .mapToDouble(MythicPlusScore::getAll)
                .average()
                .orElse(0);
    }
}
