package de.haevn.api;

import de.haevn.exceptions.NetworkException;
import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.model.DiscordWebhook;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.SerializationUtils;

import java.util.List;

public class DiscordApi extends AbstractApi {
    private static final Logger LOGGER = LoggerHandler.get(DiscordApi.class);
    private static final DiscordApi INSTANCE = new DiscordApi();

    private DiscordApi() {
        super();
    }

    public static DiscordApi getInstance() {
        return INSTANCE;
    }

    public void sendLogWebhook(String url) {
        final String webhook = PropertyHandler.getInstance("config").get("urls.webhook.log");

        DiscordWebhook data = new DiscordWebhook();
        DiscordWebhook.Embed embed = new DiscordWebhook.Embed();
        data.setUsername("Log uploader");
        data.setContent("");
        embed.setUrl(url);
        embed.setTitle("New Log is published");
        embed.setDescription("You can view the newly published log via " + url);
        embed.setColor(12390624);
        data.setEmbeds(List.of(embed));

        SerializationUtils.exportJson(data).ifPresent(message -> sendWebhook(webhook, message));

    }


    public void sendRecordWebhook(String url) {
        final String webhook = PropertyHandler.getInstance("config").get("urls.webhook.log");

        DiscordWebhook data = new DiscordWebhook();
        DiscordWebhook.Embed embed = new DiscordWebhook.Embed();
        data.setUsername("Video record uploader");
        data.setContent("");
        embed.setUrl(url);
        embed.setTitle("New video is published");
        embed.setDescription("You can view the newly published video via " + url);
        embed.setColor(12390624);
        data.setEmbeds(List.of(embed));

        SerializationUtils.exportJson(data).ifPresent(message -> sendWebhook(webhook, message));

    }

    public void sendWebhook(String webhook, String message) {
        try {
            NetworkUtils.sendPostRequest(webhook, message);
        } catch (NetworkException ex) {
            LOGGER.atError(ex.getMessage(), ex);
        }
    }
}
