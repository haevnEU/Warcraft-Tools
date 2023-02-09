package de.haevn.utils;

import java.text.NumberFormat;

public class MathUtils {
    private MathUtils(){}



    public static NumberFormat getDecimalFormat() {
        return NumberFormat.getInstance();
    }

    public static String msToString(double milliseconds){
        return msToString(Math.round(milliseconds));
    }

    public static String numberToRoundText(double number) {
        return Integer.toString((int) Math.round(number));
    }

    public static String msToString(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        String negative = "";
        if (minutes < 0) {
            minutes *= -1;
            negative = "-";
        }
        if (seconds < 0) {
            seconds *= -1;
            negative = "-";
        }
        return negative + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}
