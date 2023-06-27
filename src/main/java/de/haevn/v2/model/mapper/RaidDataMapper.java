package de.haevn.v2.model.mapper;

import de.haevn.v2.model.RaidData;
import de.haevn.v2.model.dto.raid.Rank;
import de.haevn.v2.model.dto.raid.Ranking;

import java.util.Map;

public class RaidDataMapper {
    private final RaidData data = new RaidData();

    public RaidDataMapper() {
        super();
    }

    public RaidDataMapper addNormalRaidRanking(String slug, Ranking ranking) {
        return addRaidRanking(data.getNormalRankings(), slug, ranking);

    }

    public RaidDataMapper addHeroicRaidRanking(String slug, Ranking ranking) {
        return addRaidRanking(data.getHeroicRankings(), slug, ranking);

    }

    public RaidDataMapper addMythicRaidRanking(String slug, Ranking ranking) {
        return addRaidRanking(data.getMythicRankings(), slug, ranking);

    }

    private RaidDataMapper addRaidRanking(Map<String, Ranking> map, String slug, Ranking ranking) {
        map.put(slug, ranking);
        return this;
    }

    public RaidData build() {
        return data;
    }
}
