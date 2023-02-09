package de.haevn.model.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RatingDefinition {

    private RatingValue<Integer> minKeystoneLevelTimed;

    private RatingValue<Integer> amountTimedWeekly;

    private RatingValue<Integer> minTimeRemaining;


    //----------------------------------------------------------------------------------------------------------------------
    //  Score
    //----------------------------------------------------------------------------------------------------------------------

    private RatingValue<Integer> minScore;
    private RatingValue<Integer> maxScore;
    private RatingValue<Integer> perfect;
    private RatingValue<Integer> previous;


    //----------------------------------------------------------------------------------------------------------------------
    //  Dungeon
    //----------------------------------------------------------------------------------------------------------------------

    private RatingValue<Integer> maxKeystoneLevelTimed;
    private RatingValue<Integer> minKeystoneLevel;
    private RatingValue<Integer> maxKeystoneLevel;


    //----------------------------------------------------------------------------------------------------------------------
    //  GENERAL
    //----------------------------------------------------------------------------------------------------------------------

    private List<RatingValue<String>> faction;

    private List<RatingValue<String>> realm;

    private List<RatingValue<String>> country;


    //----------------------------------------------------------------------------------------------------------------------
    //  Time
    //----------------------------------------------------------------------------------------------------------------------

    private RatingValue<Integer> minTime;



    @JsonProperty("general")
    private void unpackNestedGeneral( Map<String, List<RatingValue<String>>> general) {
        this.faction = general.get("faction");
        this.realm = general.get("realm");
        this.country = general.get("country");
    }

    @JsonProperty("dungeon")
    private void unpackNestedDungeon( Map<String, RatingValue<Integer>> dungeon) {
        this.maxKeystoneLevel = dungeon.get("maxKeystoneLevel");
        this.minKeystoneLevel = dungeon.get("minKeystoneLevel");
        this.maxKeystoneLevelTimed = dungeon.get("maxKeystoneLevelTimed");
    }

    @JsonProperty("score")
    private void unpackNestedScore( Map<String, RatingValue<Integer>> score) {
        this.minScore = score.get("minScore");
        this.maxScore = score.get("maxScore");
        this.previous = score.get("previous");
        this.perfect = score.get("perfect");
    }

    @JsonProperty("time")
    private void unpackNestedTime( Map<String, RatingValue<Integer>> times) {
        this.minTime = times.get("min");
    }


    @Data
    public static class RatingValue<T> {
        T value;
        double rating = 0.0;
    }
}
