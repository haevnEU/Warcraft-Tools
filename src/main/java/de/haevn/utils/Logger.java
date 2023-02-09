package de.haevn.utils;

import java.time.Instant;
import java.util.Date;

public class Logger {

    private final Class<?> cl;

    public Logger(Class<?> cl) {
        this.cl = cl;
    }

    public void trace(String message, String... args) {
        log(Level.TRACE, message, args);
    }

    public void debug(String message, String... args) {
        log(Level.DEBUG, message, args);
    }

    public void info(String message, String... args) {
        log(Level.INFO, message, args);
    }

    public void notice(String message, String... args) {
        log(Level.NOTICE, message, args);
    }

    public void warn(String message, String... args) {
        log(Level.WARNING, message, args);
    }

    public void err(String message, String... args) {
        err(message, null, args);
    }

    public void err(String message, Throwable throwable, String... args) {
        log(Level.ERROR, message, throwable, args);
    }

    public void fatal(String message, String... args) {
        fatal(message, null, args);
    }

    public void fatal(String message, Throwable throwable, String... args) {
        log(Level.FATAL, message, throwable, args);
    }

    private void log(Level level, String message, String... args) {
        log(level, message, null, args);
    }

    private void log(Level level, String message, Throwable throwable, String... args) {
        String stacktrace = "";
        if (null != throwable) {
            stacktrace = ExceptionUtils.getStackTrace(throwable);
            stacktrace = stacktrace.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f");
        }

        String json = "{" +
                "\"Time\":\"%s\"," +
                "\"level\": \"%s\"," +
                " \"class\": \"%s\"," +
                " \"message\": \"%s\"," +
                " \"stacktrace\": \"%s\"," +
                " \"args\": \"%s\"}";

        var logger = java.util.logging.Logger.getLogger(Logger.class.getName());
        StringBuilder msg = new StringBuilder();
        msg.append("[").append(level).append("] ").append(cl.getSimpleName()).append(": ").append(message).append(";JSON\n").append(String.format(json,
                Date.from(Instant.now()),
                level,
                cl.getSimpleName(),
                message,
                stacktrace,
                String.join(",", args)));
        logger.warning(msg.toString());
    }

    public enum Level {
        TRACE("TRACE"),
        DEBUG("DEBUG"),
        INFO("INFO"),
        NOTICE("NOTICE"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        FATAL("FATAL");

        private final String label;

        private Level(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
