package de.haevn.enumeration;

public enum Season {

    DF_SEASON_1("season-df-1"),
    SL_SEASON_4("season-sl-4"),
    SL_SEASON_3("season-sl-3"),
    NONE("");

    public final String label;

    Season(String label) {
        this.label = label;
    }

    public static Season getCurrentSeasonKey() {
        return DF_SEASON_1;
    }

    public static Season getPreviousSeasonKey() {
        return SL_SEASON_4;
    }

    public static Season getSeasonByName(String name) {
        return switch (name.toLowerCase()) {
            case "season-df-1", "df-1":
                yield Season.DF_SEASON_1;
            case "season-sl-4", "sl-4":
                yield Season.SL_SEASON_4;
            case "season-sl-3", "sl-3":
                yield Season.SL_SEASON_3;
            default:
                yield Season.NONE;
        };
    }

    @Override
    public String toString() {
        return label;
    }
}
