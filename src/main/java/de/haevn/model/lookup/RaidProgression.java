package de.haevn.model.lookup;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public final class RaidProgression {

    @JsonAlias("vault-of-the-incarnates")
    private Raid vaultOfTheIncarnates;
    @JsonAlias("aberrus-the-shadowed-crucible")
    private Raid aberrusTheShadowedCrucible;

    @JsonIgnore
    public List<Raid> toList() {
        vaultOfTheIncarnates.name = "Vault of the Incarnates";
        aberrusTheShadowedCrucible.name = "Aberrus the Shadowed Crucible";
        return List.of(aberrusTheShadowedCrucible, vaultOfTheIncarnates);
    }

    @Data
    public static class Raid {
        @JsonIgnore
        private String name = "Unknown";

        private String summary;
        @JsonAlias("total_bosses")
        private int totalBosses;
        @JsonAlias("normal_bosses_killed")
        private int normalBossesKilled;
        @JsonAlias("heroic_bosses_killed")
        private int heroicBossesKilled;
        @JsonAlias("mythic_bosses_killed")
        private int mythicBossesKilled;

        @Override
        public String toString() {
            return name;
        }
    }
}
