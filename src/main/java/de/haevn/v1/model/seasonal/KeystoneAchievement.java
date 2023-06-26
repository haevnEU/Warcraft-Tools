package de.haevn.v1.model.seasonal;

import lombok.Data;

@Data
public final class KeystoneAchievement {
    private int score;
    private Faction horde = new Faction();
    private Faction alliance = new Faction();
    private Faction all = new Faction();

}
