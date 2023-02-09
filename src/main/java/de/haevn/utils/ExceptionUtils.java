package de.haevn.utils;

public class ExceptionUtils {
    private ExceptionUtils() {}

    public static String getStackTrace(Throwable throwable) {
        var sw = new java.io.StringWriter();
        var pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
