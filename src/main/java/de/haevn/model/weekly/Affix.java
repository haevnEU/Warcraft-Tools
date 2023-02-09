package de.haevn.model.weekly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Comparator;

@Data
public class Affix {
    private String id;
    private String name;
    private String description;
    @JsonProperty("wowhead_url")
    private String wowheadUrl;

    @JsonIgnore
    public static final Comparator<Affix> COMPARE = (current, target) -> current.id.equalsIgnoreCase(target.id) ? 0 : 1;

}
