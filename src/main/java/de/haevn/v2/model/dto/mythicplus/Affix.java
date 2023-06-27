package de.haevn.v2.model.dto.mythicplus;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Affix {
    private int id;
    private String name;
    private String description;
    @JsonAlias("wowhead_url")
    private String wowheadUrl;
}
