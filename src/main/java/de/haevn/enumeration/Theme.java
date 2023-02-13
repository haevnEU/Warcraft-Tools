package de.haevn.enumeration;

public enum Theme {
    HORDE("horde"),
    ALLIANCE("alliance"),
    DARK("dark"),
    LIGHT("light"),
    PALADIN("paladin"),
    DEATHKNIGHT("deathknight"),
    DEMONHUNTER("demonhunter"),
    DRUID("druid"),
    EVOKER("evoker"),
    HUNTER("hunter"),
    MAGE("mage"),
    MONK("monk"),
    PRIEST("priest"),
    ROGUE("rogue"),
    SHAMAN("shaman"),
    WARLOCK("warlock"),
    WARRIOR("warrior");

    public final String label;
    private Theme(String label){
        this.label = label;
    }

}
