package de.haevn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public final class CountryRealm {
    private List<String> germany = new ArrayList<>();
    private List<String> england = new ArrayList<>();
    private List<String> spain = new ArrayList<>();
    private List<String> france = new ArrayList<>();
    private List<String> italy = new ArrayList<>();
    private List<String> russia = new ArrayList<>();
    @JsonProperty("russia_clear")
    private List<String> russiaClear = new ArrayList<>();

    public List<String> getRealmsFor(String country) {
        return switch (country.toUpperCase()) {
            case "DE":
                yield germany;
            case "GB":
                yield england;
            case "ES":
                yield spain;
            case "FR":
                yield france;
            case "IT":
                yield italy;
            case "RU":
                yield russiaClear;
            case "RU_CYRILLIC", "RU_CYR":
                yield russia;
            default:
                yield new ArrayList<>();
        };
    }

    public String getCountry(String realm) {
        Predicate<String> realmPredicate = r -> r.equalsIgnoreCase(realm);
        String result = "NONE";
        result = germany.stream().anyMatch(realmPredicate) ? "DE" : result;
        result = england.stream().anyMatch(realmPredicate) ? "GB" : result;
        result = spain.stream().anyMatch(realmPredicate) ? "ES" : result;
        result = france.stream().anyMatch(realmPredicate) ? "FR" : result;
        result = italy.stream().anyMatch(realmPredicate) ? "IT" : result;
        result = russiaClear.stream().anyMatch(realmPredicate) ? "RU" : result;
        result = russia.stream().anyMatch(realmPredicate) ? "RU_CYRILLIC" : result;
        return result;
    }
}
