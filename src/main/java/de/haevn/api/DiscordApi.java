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
        sendLogWebhook(url, "You can view the newly published log via " + url);
    }

    public void sendLogWebhook(String url, String message) {
        final String webhook = PropertyHandler.getInstance("config").get("urls.webhook.log");

        DiscordWebhook data = new DiscordWebhook();
        DiscordWebhook.Embed embed = new DiscordWebhook.Embed();
        data.setUsername("Log uploader");
        data.setContent("");
        embed.setUrl(url);
        embed.setTitle("Log is published");
        embed.setDescription(message);
        embed.setColor(7482272);
        data.setEmbeds(List.of(embed));
        SerializationUtils.exportJson(data).ifPresent(msg -> sendWebhook(webhook, msg));

    }


    public void sendRecordWebhook(String url) {
        sendRecordWebhook(url, "You can view the newly published video via " + url);
    }

    public void sendRecordWebhook(String url, String message) {
        final String webhook = PropertyHandler.getInstance("config").get("urls.webhook.log");

        DiscordWebhook data = new DiscordWebhook();
        DiscordWebhook.Embed embed = new DiscordWebhook.Embed();
        data.setUsername("Video record uploader");
        data.setContent("");
        embed.setUrl(url);
        embed.setTitle("Video is published");
        embed.setDescription(message);
        embed.setColor(2841248);
        data.setEmbeds(List.of(embed));

        SerializationUtils.exportJson(data).ifPresent(msg -> sendWebhook(webhook, msg));

    }

    public void sendWebhook(String webhook, String message) {
        try {
            NetworkUtils.sendPostRequest(webhook, message);
        } catch (NetworkException ex) {
            LOGGER.atError(ex.getMessage(), ex);
        }
    }
}
