package de.haevn.model.lookup;

import com.fasterxml.jackson.annotation.JsonAlias;
import de.haevn.model.weekly.Affix;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public final class MythicPlusDungeon {
    private String dungeon = "";
    @JsonAlias("short_name")
    private String shortName = "";
    private String url;
    @JsonAlias("completed_at")
    private Date completedAt;
    @JsonAlias("mythic_level")
    private int mythicLevel = 0;
    @JsonAlias("clear_time_ms")
    private long clearTimeMs = 0;
    @JsonAlias("par_time_ms")
    private long parTimeMs = 0;
    @JsonAlias("num_keystone_upgrades")
    private int numKeystoneUpgrades = 0;
    private int score = 0;
    private List<Affix> affixes;


    public boolean isTyrannical() {
        return !isFortified();
    }

    public boolean isFortified() {
        return affixes.stream().anyMatch(affix -> affix.getName().equalsIgnoreCase("Fortified"));
    }

    @Override
    public String toString() {
        final String upgradeTendency = parTimeMs - clearTimeMs > 0 ? "▲" : "▼";
        return "[" + mythicLevel + "] " + upgradeTendency + " " + dungeon + " (" + shortName + ") ";
    }
}
