package de.haevn.api;

public class DiscordApi extends AbstractApi {
    private static final DiscordApi INSTANCE = new DiscordApi();

    private DiscordApi() {
        super();
    }

    public static DiscordApi getInstance() {
        return INSTANCE;
    }


}
