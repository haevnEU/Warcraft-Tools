package de.haevn.model.rating;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MythicPlusScoreMapping {
    private double perfect;
    private double min;
    private Map<Integer, Double> scores = new HashMap<>();
}
