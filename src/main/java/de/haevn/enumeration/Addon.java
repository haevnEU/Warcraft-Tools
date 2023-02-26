package de.haevn.enumeration;

import java.util.Map;

public enum Addon {


    DRAGONFLIGHT("9"),
    SHADOWLANDS("8"),
    NONE("");

    public static final Map<Addon, Season[]> getSeasons = Map.of(
            Addon.DRAGONFLIGHT, new Season[]{Season.DF_SEASON_1},
            Addon.SHADOWLANDS, new Season[]{Season.SL_SEASON_3, Season.SL_SEASON_4}
    );
    public final String label;

    Addon(String label) {
        this.label = label;
    }

    public static Addon getAddonByName(String name) {
        return switch (name.toLowerCase()) {
            case "dragonflight", "9":
                yield Addon.DRAGONFLIGHT;
            case "shadowlands", "8":
                yield Addon.SHADOWLANDS;
            default:
                yield Addon.NONE;
        };
    }
}
