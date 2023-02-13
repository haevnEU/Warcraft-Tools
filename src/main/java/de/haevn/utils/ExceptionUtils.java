package de.haevn.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static String getStackTrace(Throwable throwable) {
        final StringWriter sw = new java.io.StringWriter();
        final PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
