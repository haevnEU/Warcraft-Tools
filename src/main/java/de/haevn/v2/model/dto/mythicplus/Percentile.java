package de.haevn.v2.model.dto.mythicplus;

import lombok.Data;

@Data
public class Percentile {
    private double quantile;
    private double quantileMinValue;
    private int quantilePopulationCount;
    private double quantilePopulationFraction;
    private int totalPopulationCount;
}
