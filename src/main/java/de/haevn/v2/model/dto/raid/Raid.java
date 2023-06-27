package de.haevn.v2.model.dto.raid;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Raid {
    private int id;
    private String slug;
    private String name;
    @JsonAlias("short_name")
    private String shortName;
    private ArrayList<Encounter> encounters;
    private RaidTime starts;
    private RaidTime ends;

    private List<Ranking> normalRankings = new ArrayList<>();
    private List<Ranking> heroicRankings = new ArrayList<>();
    private List<Ranking> mythicRankings = new ArrayList<>();
}
