package de.haevn.logging;

import java.util.HashMap;
import java.util.Map;

public class LoggerHandler {

    private static final Map<String, Logger> loggers = new HashMap<>();

    public static <T> Logger get(Class<T> cl) {
        if (!loggers.containsKey(cl.getCanonicalName())) {
            loggers.put(cl.getCanonicalName(), new Logger(cl));
        }
        return loggers.get(cl.getCanonicalName());
    }

    public static void flush() {
        loggers.values().forEach(Logger::flush);
    }

    public enum Level {
        NONE,
        INFO,
        WARNING,
        ERROR,
        DEBUG
    }
}
