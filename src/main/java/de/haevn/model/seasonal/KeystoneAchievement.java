package de.haevn.model.seasonal;

import lombok.Data;

@Data
public class KeystoneAchievement {
    private int score;
    private Faction horde = new Faction();
    private Faction alliance = new Faction();
    private Faction all = new Faction();


    public static KeystoneAchievement returnNewIfNull(KeystoneAchievement achievement){
        if(null != achievement) return  achievement;
        KeystoneAchievement ksa = new KeystoneAchievement();
        ksa.score = 0;
        Faction faction = new Faction();
        faction.setQuantile(0);
        faction.setQuantileMinValue(0);
        faction.setQuantilePopulationCount(0);
        faction.setQuantilePopulationFraction(0);
        faction.setTotalPopulationCount(0);
        ksa.all = faction;
        ksa.horde = faction;
        ksa.alliance = faction;

        return ksa;
    }
}
