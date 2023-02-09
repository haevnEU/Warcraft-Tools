package de.haevn.enumeration;

public enum RatingEnum {

    POSITIVE(""), NEGATIVE(""), NEUTRAL("");

    public final String label;

    RatingEnum(String label) {
        this.label = label;
    }

    public static RatingEnum getRatingByNumber(int number) {
        if (number == 0) return RatingEnum.NEUTRAL;
        else if (number > 0) return RatingEnum.POSITIVE;
        else return RatingEnum.NEGATIVE;
    }
}
