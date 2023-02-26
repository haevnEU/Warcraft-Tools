package de.haevn.enumeration;

public enum Season {

    DF_SEASON_1("season-df-1", "DF Season 1"),
    SL_SEASON_4("season-sl-4", "SL Season 4"),
    SL_SEASON_3("season-sl-3", "SL Season 3"),
    NONE();

    public final String label;
    public final String slug;

    Season() {
        this("", "");
    }

    Season(String label, String slug) {
        this.label = label;
        this.slug = slug;
    }

    public static Season getCurrentSeasonKey() {
        return DF_SEASON_1;
    }

    public static Season getPreviousSeasonKey() {
        return SL_SEASON_4;
    }

    public static Season getPreviousPreviousSeasonKey() {
        return SL_SEASON_3;
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
        return slug;
    }
}
