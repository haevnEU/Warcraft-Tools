package de.haevn.enumeration;

public enum FactionEnum {
    NONE(""),
    ALL("All"),
    HORDE("Horde"),
    ALLIANCE("Alliance");

    private final String label;

    FactionEnum(String label) {
        this.label = label;
    }

    public static FactionEnum getFactionByName(String name) {
        return switch (name.toLowerCase()) {
            case "alliance":
                yield ALLIANCE;
            case "horde":
                yield HORDE;
            default:
                yield ALL;
        };
    }

    @Override
    public String toString() {
        return label;
    }
}
