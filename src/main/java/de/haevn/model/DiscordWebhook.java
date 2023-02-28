package de.haevn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DiscordWebhook {

    @JsonProperty("avatar_url")
    private String avatarUrl = "";
    private String username;
    private String content;
    private List<Embed> embeds;

    @Data
    public static class Embed {
        private String url;
        private String title;
        private String description;
        private long color;

        private Thumbnail thumbnail;
        private List<Field> fields;
        private Image image;
        private Footer footer;

    }

    @Data
    public static class Thumbnail {
        private String url;
    }

    @Data
    public static class Image {
        private String url;
    }

    @Data
    public static class Field {
        private String name;
        private String value;
        private boolean inline;
    }

    @Data
    public static class Footer {
        private String text;
        @JsonProperty("icon_url")
        private String iconUrl;
    }
}
