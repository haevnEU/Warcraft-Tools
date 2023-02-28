package de.haevn.utils;

import javafx.scene.paint.Color;

public final class ColorUtils {
    private ColorUtils() {
    }

    public static long colorToInt(long r, long g, long b, long a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static long colorToInt(long r, long g, long b) {
        return colorToInt(r, g, b, 255);
    }

    public static long colorToInt(double r, double g, double b, double a) {
        return colorToInt((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
    }

    public static long colorToInt(double r, double g, double b) {
        return colorToInt(r, g, b, 1);
    }

    public static long colorToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public static long colorToInt(String hex, long a) {
        return colorToInt(hex) | (a << 24);
    }

    public static long colorToInt(String hex, double a) {
        return colorToInt(hex, (int) (a * 255));
    }

    public static Color intToColor(long color) {
        return new Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
    }

    public static Color intToColor(String hex) {
        return intToColor(colorToInt(hex));
    }

    public static Color intToColor(String hex, long a) {
        return intToColor(colorToInt(hex, a));
    }

    public static Color intToColor(String hex, double a) {
        return intToColor(hex, (int) (a * 255));
    }

    public static String colorToHex(long color) {
        return String.format("%06X", color & 0xFFFFFF);
    }

    public static String colorToHex(Color color) {
        return colorToHex(colorToInt(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
    }

    public static String colorToHex(String hex) {
        return colorToHex(colorToInt(hex));
    }

    public static String colorToHex(String hex, long a) {
        return colorToHex(colorToInt(hex, a));
    }

    public static String colorToHex(String hex, double a) {
        return colorToHex(hex, (int) (a * 255));
    }

    public static String colorToHex(long r, long g, long b, long a) {
        return colorToHex(colorToInt(r, g, b, a));
    }

    public static String colorToHex(long r, long g, long b) {
        return colorToHex(colorToInt(r, g, b));
    }

    public static String colorToHex(double r, double g, double b, double a) {
        return colorToHex(colorToInt(r, g, b, a));
    }

    public static String colorToHex(double r, double g, double b) {
        return colorToHex(colorToInt(r, g, b));
    }
}
