package de.haevn.utils;

import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;

import java.util.Properties;

public final class AppData {
    private final Properties properties = new Properties();
    private static final Logger LOGGER = LoggerHandler.get(AppData.class);

    private AppData() {
        load();
    }

    private static final AppData INSTANCE = new AppData();

    public static AppData getInstance() {
        return INSTANCE;
    }

    public void load() {
        try {
            LOGGER.atInfo("Loading app.config");
            properties.load(getClass().getResourceAsStream("/app.config"));

        } catch (Exception ex) {
            LOGGER.atError("Could not load app.config, shutting down application", ex);
            System.exit(1);
        }
    }


    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String get(String key) {
        return get(key, "");
    }

    public String getName() {
        return get("app.name");
    }

    public String getVersion() {
        return get("app.version");
    }

    public String getBuild() {
        return get("app.build");
    }


}
