package de.haevn.v2.model.mapper;

import de.haevn.v2.model.Addon;
import de.haevn.v2.model.dto.raid.Raid;

public class AddonMapper {
    private final Addon addon = new Addon();

    public void addRaid(String slug, Raid raid){
        addon.getRaids().put(slug, raid);
    }

    public Addon build() {
        return addon;
    }
}
