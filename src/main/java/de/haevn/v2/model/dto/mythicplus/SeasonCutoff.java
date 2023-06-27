package de.haevn.v2.model.dto.mythicplus;

import lombok.Data;

@Data
public class SeasonCutoff {
    private String updatedAt;
    private Cutoff p999;
    private Cutoff p990;
    private Cutoff p900;
    private Cutoff p750;
    private Cutoff p600;

    private Cutoff keystoneHero;
    private Cutoff keystoneMaster;
    private Cutoff keystoneConqueror;
    private Cutoff keystoneExplorer;

}
