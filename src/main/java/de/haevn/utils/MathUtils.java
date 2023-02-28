package de.haevn.utils;

import java.text.NumberFormat;

public final class MathUtils {
    private MathUtils() {
    }


    public static NumberFormat getDecimalFormat() {
        return NumberFormat.getInstance();
    }


    public static int hexToInteger(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static String numberToRoundText(double number) {
        return Integer.toString((int) Math.round(number));
    }

    public static Integer stringToInteger(String string, int fallbackNumber) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return fallbackNumber;
        }
    }

    public static Integer stringToInteger(String string) {
        return stringToInteger(string, 0);
    }

    public static Double stringToDouble(String string, double fallbackNumber) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return fallbackNumber;
        }
    }

    public static Double stringToDouble(String string) {
        return stringToDouble(string, 0.0);
    }


    public static String numberToShortValue(int number) {
        if (number < 1000) return Integer.toString(number);
        if (number < 1000000) return getDecimalFormat().format(number / 1000.0) + "K";
        if (number < 1000000000) return getDecimalFormat().format(number / 1000000.0) + "M";
        return getDecimalFormat().format(number / 1000000000.0) + "B";
    }

    public static String numberToShortValue(double number) {
        return numberToShortValue((int) number);
    }


    public static String msToString(double milliseconds) {
        return msToString(Math.round(milliseconds));
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
