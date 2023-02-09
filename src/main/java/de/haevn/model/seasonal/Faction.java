package de.haevn.model.seasonal;

import lombok.Data;

@Data
public class Faction {
    private double quantile;
    private double quantileMinValue;
    private int quantilePopulationCount;
    private double quantilePopulationFraction;
    private int totalPopulationCount;
}
