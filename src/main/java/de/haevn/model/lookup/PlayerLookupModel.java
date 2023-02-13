package de.haevn.model.lookup;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public final class PlayerLookupModel {

    private String name;
    private String realm;
    private String race;
    @JsonAlias("active_spec_name")
    private String activeSpecName;
    @JsonAlias("active_spec_role")
    private String activeSpecRole;

    private String faction;

    @JsonAlias("class")
    private String characterClass;
    @JsonAlias("thumbnail_url")
    private String thumbnailUrl;

    @JsonAlias("profile_url")
    private String profileUrl;
    @JsonAlias("last_crawled_at")
    private Date lastCrawledAt;

    @JsonAlias("raid_progression")
    private RaidProgression raidProgression;
    @JsonAlias("mythic_plus_highest_level_runs")
    private List<MythicPlusDungeon> mythicPlusHighestLevelRuns;
    @JsonAlias("mythic_plus_best_runs")
    private List<MythicPlusDungeon> mythicPlusBestRuns;
    @JsonAlias("mythic_plus_recent_runs")
    private List<MythicPlusDungeon> mythicPlusRecentRuns;
    @JsonAlias("mythic_plus_scores_by_season")
    private List<MythicPlusScore> mythicPlusScoresBySeason;

}
