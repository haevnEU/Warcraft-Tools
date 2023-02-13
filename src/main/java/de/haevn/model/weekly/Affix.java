package de.haevn.model.weekly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Comparator;

@Data
public final class Affix {
    @JsonIgnore
    public static final Comparator<Affix> COMPARE = (current, target) -> current.id.equalsIgnoreCase(target.id) ? 0 : 1;
    private String id;
    private String name;
    private String description;
    @JsonProperty("wowhead_url")
    private String wowheadUrl;

}
