package de.haevn.v2.model;

import de.haevn.v2.model.dto.raid.Ranking;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RaidData {
    private Map<String, Ranking> normalRankings = new HashMap<>();
    private Map<String, Ranking> heroicRankings = new HashMap<>();
    private Map<String, Ranking> mythicRankings = new HashMap<>();
}
