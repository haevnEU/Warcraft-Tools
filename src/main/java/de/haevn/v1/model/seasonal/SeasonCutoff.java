package de.haevn.v1.model.seasonal;

import de.haevn.v1.enumeration.Season;
import lombok.Data;

import java.util.Date;

@Data
public final class SeasonCutoff {
    private String seasonKey;
    private Date updatedAt;
    // 0.1%
    private Percentile p999;
    // 1%
    private Percentile p990;
    // 10%
    private Percentile p900;
    // 25%
    private Percentile p750;
    // 40%
    private Percentile p600;
    private KeystoneAchievement keystoneHero;
    private KeystoneAchievement keystoneMaster;
    private KeystoneAchievement keystoneConqueror;
    private KeystoneAchievement keystoneExplorer;

    public boolean isCurrent() {
        return seasonKey.equals(Season.getCurrentSeasonKey().label);
    }
}
