package de.haevn.v2.model;

import de.haevn.v2.model.dto.raid.Raid;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Addon {
    private Map<String, Raid> raids = new HashMap<>();
    private Map<String, MythicPlusSeasonalData> mythicPlusSeasonalData = new HashMap<>();

    public void addRaid(String slug, Raid raid) {
        raids.put(slug, raid);
    }
}
