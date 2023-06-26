package de.haevn.v1.utils;

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


    GIT_URL("git.url"),
    GIT_URL_SEASONAL("git.url.seasonal"),
    GIT_URL_DEFINITION("git.url.definition"),
    GIT_URL_SCORE_MAP("git.url.scoreMap"),
    GIT_URL_AFFIX_COMBO("git.url.affixCombo"),
    GIT_URL_REALM("git.url.realm"),

    RIO_URL_CURRENT_AFFIX("rio.url.currentAffix"),
    RIO_URL_CUTOFF("rio.url.cutoff"),
    RIO_URL_CURRENT_CUTOFF("rio.url.cutoff.current"),
    RIO_URL_CHARACTER("rio.url.character"),
    RIO_QUERY_CHARACTER("rio.query.character"),
    RIO_QUERY_CHARACTER_SEASONS("rio.query.character.seasons"),
    RIO_URL_STATIC_DATA("rio.url.staticData"),


    SUPPORT_REPO("git.url"),
    SUPPORT_ISSUE("support.issue"),
    SUPPORT_FEATURE("support.feature"),
    SUPPORT_CRASH("support.crash"),

    ;

    public final String value;

    PropertyKeys(String value) {
        this.value = value;
    }

}
