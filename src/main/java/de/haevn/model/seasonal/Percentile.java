package de.haevn.model.seasonal;

import lombok.Data;

@Data
public final class Percentile {
    private Faction horde = new Faction();
    private Faction alliance = new Faction();
    private Faction all = new Faction();
}
