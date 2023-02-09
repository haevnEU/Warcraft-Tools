package de.haevn.utils;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.enumeration.Season;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.model.rating.MythicPlusScoreMapping;
import de.haevn.model.rating.RatingDefinition;
import de.haevn.model.seasonal.SeasonCutoff;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ScoreUtils {
    private static final ScoreUtils INSTANCE = new ScoreUtils();
    public static ScoreUtils getInstance() {
        return INSTANCE;
    }

    private final SimpleObjectProperty<MythicPlusScoreMapping> scoreMapping = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RatingDefinition> ratingDefinition = new SimpleObjectProperty<>();
    private ScoreUtils() {
        GitHubApi.getInstance().getMythicPlusScoreMappingProperty().addListener((observable, oldValue, newValue) -> scoreMapping.set(newValue));
        GitHubApi.getInstance().getRatingDefinition().addListener((observable, oldValue, newValue) -> ratingDefinition.set(newValue));
    }

    public String getPercentileForScore(double score, Season season) {
        var cutoffs = RaiderIOApi.getInstance().getCutoffs().get();

        Predicate<SeasonCutoff> filter = cutoff -> cutoff.getSeasonKey().equalsIgnoreCase(season.label);

        List<SeasonCutoff> cuttofList = new ArrayList<>();

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


    public RatingResult rateScoreForCurrentSeason(PlayerLookupModel player, double level){
        return rateScoreForSeason(player, level, Season.getCurrentSeasonKey());
    }

    public RatingResult rateScoreForPreviousSeason(PlayerLookupModel player, double level){
        return rateScoreForSeason(player, level, Season.getPreviousSeasonKey());
    }

    public RatingResult rateScoreForSeason(PlayerLookupModel player, double level, Season season){
        return new RatingResult();
    }


    public static class RatingResult{
        private static final String FORMULA = "";
        private String rating = "";
        private String description = "";

        public String getRating() {
            return rating;
        }

        public String getDescription() {
            return description;
        }

        public String getFormula() {
            return FORMULA;
        }
    }
}
