package de.haevn.logging;

import de.haevn.Main;
import de.haevn.utils.ExceptionUtils;
import de.haevn.utils.FileIO;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static boolean buffered = false;
    private static boolean redirect = true;
    private static PrintStream output = System.out;
    private final List<String> entries = new ArrayList<>(50);
    private final String name;
    private final String filename;

    <T> Logger(Class<T> cl) {
        this.name = cl.getCanonicalName();
        this.filename = cl.getSimpleName();
        log(LoggerHandler.Level.NONE,"==========BEGIN LOGGING (%s)==========", LocalDateTime.now());
        log(LoggerHandler.Level.INFO, "Logger initialized for %s", name);
    }


    public static void setBuffered(boolean buffered) {
        Logger.buffered = buffered;
    }

    public static void setOutput(PrintStream output) {
        Logger.output = output;
    }

    public static void redirectToFile(boolean redirect){
        Logger.redirect = redirect;
    }

    private void log(LoggerHandler.Level level, String message, Object... args) {
        String result = "";
        if(level == LoggerHandler.Level.NONE){
            result = String.format(message, args)+"\n";
        }else {
            final String time = LocalDateTime.now().toString();
            final String outMessage = String.format(message, args);
            result = String.format("[%s] [%s] %s: %s%n", level, time, name, outMessage);

        }
        entries.add(result);
        if(null != output){
            output.print(result);
        }
        if(!buffered){
            flush();
        }
    }

    public String getLog(){
        return String.join("", entries);
    }

    public void atInfo(String message, Object... args) {
        log(LoggerHandler.Level.INFO, message, args);
    }

    public void atWarning(String message, Object... args) {
        log(LoggerHandler.Level.WARNING, message, args);
    }

    public void atError(String message, Object... args) {
        log(LoggerHandler.Level.ERROR, message, args);
    }
    public void atError(String message, Exception ex, Object ... args) {

        log(LoggerHandler.Level.ERROR, message + "\n%s", args, ExceptionUtils.getStackTrace(ex));
    }

    public void atDebug(String message, Object... args) {
        log(LoggerHandler.Level.DEBUG, message, args);
    }

    public void flush(){
        if(redirect){
            FileIO.append("./logs/" + filename + ".log", getLog());
        }
        entries.clear();
    }
}
