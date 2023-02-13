package de.haevn.model.dungeons;

import lombok.Data;

import java.util.List;

@Data
public class Dungeons {

    private Dungeon algetharAcademy = new Dungeon("Algeth'ar Academy", "AA");
    private Dungeon rubyLifePools = new Dungeon("Ruby Life Pools", "RLP");
    private Dungeon azureVaults = new Dungeon("The Azure Vault", "AV");
    private Dungeon nokudOffensive = new Dungeon("The Nokhud Offensive", "NO");
    private Dungeon courtOfStarts = new Dungeon("Court of Stars", "COS");
    private Dungeon hallsOfValor = new Dungeon("Halls of Valor", "HOV");
    private Dungeon templeOfJadeSerpent = new Dungeon("Temple of Jade Serpent", "TJS");
    private Dungeon shadowmoonBurialGround = new Dungeon("Shadowmoon Burial Ground", "SBG");

    public List<Dungeon> getDungeons() {
        return List.of(algetharAcademy, rubyLifePools, azureVaults, nokudOffensive,
                courtOfStarts, hallsOfValor,
                templeOfJadeSerpent,
                shadowmoonBurialGround);
    }
}
