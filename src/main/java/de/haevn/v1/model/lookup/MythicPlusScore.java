package de.haevn.v1.model.lookup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public final class MythicPlusScore {

    int all;
    int dps;
    int healer;
    int tank;
    private String season;

    @SuppressWarnings("all")
    @JsonProperty("scores")
    private void unpackNestedScores(Map<String, Integer> brand) {
        this.all = brand.get("all");
        this.dps = brand.get("dps");
        this.healer = brand.get("healer");
        this.tank = brand.get("tank");
    }

}
