package de.haevn.v2.model.dto.mythicplus;

import lombok.Data;

@Data
public class Cutoff {
    private double score = 0;
    private Percentile all;
    private Percentile horde;
    private Percentile alliance;
}
