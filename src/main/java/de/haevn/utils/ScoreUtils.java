package de.haevn.utils;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.debug.MessageWidget;
import de.haevn.enumeration.Addon;
import de.haevn.enumeration.Season;
import de.haevn.model.lookup.MythicPlusScore;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.model.rating.RatingDefinition;
import de.haevn.model.seasonal.SeasonCutoff;

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

    public RatingResult rateScoreForCurrentSeason(PlayerLookupModel player, int level) {
        return rateScoreForSeason(player, level, Season.getCurrentSeasonKey());
    }

    public RatingResult rateScoreForPreviousSeason(PlayerLookupModel player, int level) {
        return rateScoreForSeason(player, level, Season.getPreviousSeasonKey());
    }


    public RatingResult rateScoreForSeason(PlayerLookupModel player, int level, Season season) {
        if (level < 10) {
            return new RatingResult();
        }
        var scoreMapping = GitHubApi.getInstance().getMythicPlusScoreMappingProperty().get();
        var ratingDefinition = GitHubApi.getInstance().getRatingDefinition().get();
        // TODO REMOVE BUILDER
        StringBuilder builder = new StringBuilder();
        builder.append("Player: ").append(player.getName()).append("\n");
        builder.append("Season: ").append(season.label).append("\n");
        builder.append("Level: ").append(level).append("\n");
        builder.append("scoreMapping: ").append("\n").append(scoreMapping).append("\n");
        builder.append("ratingDefinition: ").append("\n").append(ratingDefinition).append("\n");

        // Center player score around targeted level
        double scoreForLevel = scoreMapping.get(level);
        double playerScore = getScoreForCurrentSeason(player);

        double normalizedPlayerScore = playerScore - scoreForLevel;

        builder.append("scoreForLevel:          ").append(scoreForLevel).append("\n");
        builder.append("playerScore:            ").append(playerScore).append("\n");
        builder.append("normalizedPlayerScore:  ").append(normalizedPlayerScore).append("\n");

        // Rate the normalized score
        if (normalizedPlayerScore > ratingDefinition.getMaxScore().getValue()) {
            normalizedPlayerScore *= ratingDefinition.getMaxScore().getRating();
        } else if (normalizedPlayerScore < ratingDefinition.getMinScore().getValue()) {
            normalizedPlayerScore *= ratingDefinition.getMinScore().getRating();
        }
        builder.append("After score rating:     ").append(normalizedPlayerScore).append("\n");

        // Apply region filter
        final String realm = player.getRealm();
        final String faction = player.getFaction();
        final String region = GitHubApi.getInstance().getCountryRealms().get().getCountry(player.getRealm());


        boolean regionFilterActive = ratingDefinition.getCountry()
                .stream()
                .anyMatch(stringRatingValue -> stringRatingValue.getValue().equalsIgnoreCase(region));
        if (regionFilterActive) {
            normalizedPlayerScore *= ratingDefinition.getCountry()
                    .stream()
                    .filter(stringRatingValue -> stringRatingValue.getValue().equalsIgnoreCase(region))
                    .findFirst()
                    .map(RatingDefinition.RatingValue::getRating)
                    .orElse(.0);
        }
        // If no region filter is applied, apply the realm and faction filter

        builder.append("After region rating:    ").append(normalizedPlayerScore).append("\n");


        normalizedPlayerScore *= normalizedPlayerScore > 0 ? 1 : -1;
        builder.append("final nornalization:    ").append(normalizedPlayerScore).append("\n");
        MessageWidget.show(builder.toString());
        return new RatingResult();
    }


    public static class RatingResult {
        private static final String FORMULA = "";
        private static final String rating = "";
        private static final String description = "";

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
