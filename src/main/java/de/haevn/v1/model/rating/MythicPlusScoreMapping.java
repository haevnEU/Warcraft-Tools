package de.haevn.v1.model.rating;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public final class MythicPlusScoreMapping {
    private double perfect;
    private double min;
    private Map<Integer, Double> scores = new HashMap<>();

    public double get(int level) {
        return scores.getOrDefault(level, 0.0);
    }
}
