package de.haevn.v1.model.dungeons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Characteristics {
    @JsonProperty("Taunt")
    public boolean taunt;
    @JsonProperty("Silence")
    public boolean silence;
    @JsonProperty("Root")
    public boolean root;
    @JsonProperty("Fear")
    public boolean fear;
    @JsonProperty("Disorient")
    public boolean disorient;
    @JsonProperty("Imprison")
    public boolean imprison;
    @JsonProperty("Stun")
    public boolean stun;
    @JsonProperty("Slow")
    public boolean slow;
    @JsonProperty("Incapacitate")
    public boolean incapacitate;
    @JsonProperty("Polymorph")
    public boolean polymorph;
    @JsonProperty("Sap")
    public boolean sap;
    @JsonProperty("Banish")
    public boolean banish;


    public List<String> getActive() {
        List<String> active = new ArrayList<>();
        if (taunt) active.add("Taunt");
        if (silence) active.add("Silence");
        if (root) active.add("Root");
        if (fear) active.add("Fear");
        if (disorient) active.add("Disorient");
        if (imprison) active.add("Imprison");
        if (stun) active.add("Stun");
        if (slow) active.add("Slow");
        if (incapacitate) active.add("Incapacitate");
        if (polymorph) active.add("Polymorph");
        if (sap) active.add("Sap");
        if (banish) active.add("Banish");
        return active;
    }

    public List<String> getInactive() {
        List<String> inactive = new ArrayList<>();
        if (!taunt) inactive.add("Taunt");
        if (!silence) inactive.add("Silence");
        if (!root) inactive.add("Root");
        if (!fear) inactive.add("Fear");
        if (!disorient) inactive.add("Disorient");
        if (!imprison) inactive.add("Imprison");
        if (!stun) inactive.add("Stun");
        if (!slow) inactive.add("Slow");
        if (!incapacitate) inactive.add("Incapacitate");
        if (!polymorph) inactive.add("Polymorph");
        if (!sap) inactive.add("Sap");
        if (!banish) inactive.add("Banish");
        return inactive;
    }


}
