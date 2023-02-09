package de.haevn.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertyHandler {
    private static final String EXTENSION = ".property";
    private static final Logger LOGGER = new Logger(PropertyHandler.class);
    private static final Map<String, PropertyHandler> STRING_PROPERTY_HANDLER_HASH_MAP = new HashMap<>();

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

    private final Properties properties;
    private final boolean debug;
    private final String name;

    private PropertyHandler(String propertyName) {
        properties = new Properties();
        debug = Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG", "false"));
        this.name = propertyName;


        load();
    }

    public void reload() {
        load();
    }

    public void load() {

        String property = (debug ? "debug" : "production") + "/" + name;
        if (!property.endsWith(EXTENSION)) {
            property += EXTENSION;
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(property)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.fatal("Could not load property file: " + property, e);
        }
    }

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


    public boolean isDebug() {
        return debug;
    }

    public List<String> getAllProperties() {
        return properties.keySet().stream().map(Object::toString).toList();
    }

    public void set(String k, String value) {
        properties.setProperty(k, value);
        String root = "src/main/resources/";
        try (OutputStream os = new FileOutputStream(root + (debug ? "debug" : "production") + "/" + name + EXTENSION)) {
            properties.store(os, "Updated " + k + " to " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
