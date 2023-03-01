package de.haevn.enumeration;

public enum RegionEnum {

    EU("eu", "Europe"),
    US("us", "United States");
    public final String regionCode;
    public final String regionName;

    RegionEnum(String regionCode, String regionName) {
        this.regionName = regionName;
        this.regionCode = regionCode;
    }

    public static RegionEnum getRegionByName(String name) {
        return switch (name.toLowerCase()) {
            case "eu", "europe":
                yield EU;
            case "us", "united states":
                yield US;
            default:
                yield EU;
        };
    }

    @Override
    public String toString() {
        return regionName;
    }
}
