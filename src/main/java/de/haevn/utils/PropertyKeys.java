package de.haevn.utils;

public enum PropertyKeys {

    APP_REGION("app.region"),
    APP_LICENSE("app.license"),
    APP_THEME("app.theme"),
    APP_VERSION("app.version"),
    APP_BUILD("app.build"),
    APP_NAME("app.name"),

    BACKUP_WOW_AUTO("backup.wow.auto"),
    BACKUP_WOW_PATH("backup.wow.path"),

    NETWORK_REFRESH("network.refresh"),
    NETWORK_TIMEOUT("network.timeout"),



    SEASONAL_KEY("git.url.seasonal"),
    DEFINITION_KEY("git.url.definition"),
    SCORE_MAP_KEY("git.url.scoreMap"),
    AFFIX_COMBO_KEY("git.url.affixCombo"),
    REALM_KEY("git.url.realm"),

    SUPPORT_REPO("git.url"),
    SUPPORT_ISSUE("support.issue"),
    SUPPORT_FEATURE("support.feature"),

    ;

    public final String value;

    PropertyKeys(String value) {
        this.value = value;
    }

}
