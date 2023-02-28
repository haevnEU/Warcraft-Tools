package de.haevn.model;

import lombok.Data;

import java.util.List;

@Data
public class DiscordWebhook {

    private String username;
    private String content;
    private List<Embed> embeds;

    @Data
    public static class Embed {
        private String url;
        private String title;
        private String description;
        private long color;
    }

}
