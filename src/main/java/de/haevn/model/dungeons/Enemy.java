package de.haevn.model.dungeons;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Enemy {
    private String name;
    private int id;
    private int count;
    private int health;
    private int scale;
    private int displayId;
    private String creatureType;
    private int level;
    private Characteristics characteristics;
    private ArrayList<Integer> spells;
    private boolean isBoss;
    private int encounterID;
    private int instanceID;
    private boolean stealthDetect;

    @Override
    public String toString() {
        return name;
    }
}
