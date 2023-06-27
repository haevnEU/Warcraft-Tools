package de.haevn.v2.model.dto.raid;

import lombok.Data;

@Data
public class Guild {
    private int id;
    private String name;
    private String faction;
    private Realm realm;
    private Region region;
    private String path;
}
