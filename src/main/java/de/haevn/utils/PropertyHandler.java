package de.haevn.utils;

import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.ui.windows.CrashReport;

import java.io.*;
import java.util.*;

public final class PropertyHandler {
    private static final Logger LOGGER = LoggerHandler.get(PropertyHandler.class);
    private static final String EXTENSION = ".property";
    private static final Map<String, PropertyHandler> STRING_PROPERTY_HANDLER_HASH_MAP = new HashMap<>();
    private final Properties properties;
    private final String name;


    private PropertyHandler(String propertyName) {
        properties = new Properties();
        this.name = propertyName;
        load();
    }

    public static PropertyHandler getInstance(String name) {
        if (STRING_PROPERTY_HANDLER_HASH_MAP.containsKey(name.toUpperCase())) {
            return STRING_PROPERTY_HANDLER_HASH_MAP.get(name.toUpperCase());
        }

        final PropertyHandler handler = new PropertyHandler(name);
        STRING_PROPERTY_HANDLER_HASH_MAP.put(name.toUpperCase(), handler);
        return handler;
    }

    public static List<String> getAllHandler() {
        return STRING_PROPERTY_HANDLER_HASH_MAP.keySet().stream().toList();
    }

    public static void reloadAll() {
        STRING_PROPERTY_HANDLER_HASH_MAP.values().forEach(PropertyHandler::reload);
    }

    public void reload() {
        load();
    }

    public void load() {
        String property = "config/" + name;
        if (!property.endsWith(EXTENSION)) {
            property += EXTENSION;
        }
        try (InputStream inputStream = new FileInputStream(FileIO.getRootPathWithSeparator() + property)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.atError("Could not load property file: %s", property);
        }
    }

    public void store(){
        try (OutputStream os = new FileOutputStream(FileIO.getRootPathWithSeparator() + "config/" + name + EXTENSION)) {
            properties.store(os, "");
        } catch (IOException e) {
            CrashReport.show(e);
            LOGGER.atError("Could not save property file: %s", name);
        }
    }

    //----------------------------------------------------------------------------------------------------------------------
    //  Getter

    //----------------------------------------------------------------------------------------------------------------------

    public String get(String key) {
        return properties.getProperty(key, "");
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }

    public long getLong(String key) {
        return Long.parseLong(properties.getProperty(key, "0"));
    }

    public double getDouble(String key) {
        return Double.parseDouble(properties.getProperty(key, "0"));
    }

    public List<String> getAllProperties() {
        return properties.keySet().stream().map(Object::toString).toList();
    }


    public Optional<Double> getOptionalDouble(String key) {
        final String value = properties.getProperty(key, null);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Double.parseDouble(value));
    }

    public Optional<Long> getOptionalLong(String key) {
        final String value = properties.getProperty(key, null);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Long.parseLong(value));
    }

    public Optional<Boolean> getOptionalBoolean(String key) {
        final String value = properties.getProperty(key, null);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Boolean.parseBoolean(value));
    }


    public Optional<String> getOptional(String key) {
        final String value = properties.getProperty(key, null);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    public void set(String k, String value) {
        properties.setProperty(k, value);
        store();
    }
}
