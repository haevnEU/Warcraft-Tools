package de.haevn.api;

import de.haevn.enumeration.Season;
import de.haevn.exceptions.NetworkException;
import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.model.DiscordWebhook;
import de.haevn.model.lookup.MythicPlusDungeon;
import de.haevn.model.lookup.MythicPlusScore;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.utils.MathUtils;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.PropertyHandler;
import de.haevn.utils.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class DiscordApi extends AbstractApi {
    public static final String PLAYER_LOOKUP_KEY = "urls.webhook.lookup";
    public static final String LOG_KEY = "urls.webhook.log";
    public static final String RECORDING_KEY = "urls.webhook.recording";
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
        final String webhook = PropertyHandler.getInstance("config").get(LOG_KEY);

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


    public void sendRecordWebhook(String url, String title) {
        final String webhook = PropertyHandler.getInstance("config").get(RECORDING_KEY);

        DiscordWebhook data = new DiscordWebhook();
        DiscordWebhook.Embed embed = new DiscordWebhook.Embed();
        data.setUsername("Video record uploader");
        data.setContent("");
        embed.setUrl(url);
        embed.setTitle(title + " is published");
        embed.setColor(2841248);


        DiscordWebhook.Image image = new DiscordWebhook.Image();
        image.setUrl("https://avatar-resolver.vercel.app/youtube-thumbnail/q?url=" + url);
        embed.setImage(image);
        data.setEmbeds(List.of(embed));

        SerializationUtils.exportJson(data).ifPresent(msg -> sendWebhook(webhook, msg));
    }

    private Predicate<MythicPlusScore> getFilter(Season season) {
        return s -> s.getSeason().equalsIgnoreCase(season.label);
    }

    public void sendPlayerWebhook(PlayerLookupModel player) {
        final String webhook = PropertyHandler.getInstance("config").get(PLAYER_LOOKUP_KEY);

        DiscordWebhook data = new DiscordWebhook();
        data.setUsername("Player lookup");
        data.setContent("");


        final double current = player.getMythicPlusScoresBySeason()
                .stream()
                .filter(getFilter(Season.getCurrentSeasonKey()))
                .findFirst()
                .orElseGet(MythicPlusScore::new)
                .getAll();

        DiscordWebhook.Embed metaEmbed = new DiscordWebhook.Embed();
        metaEmbed.setUrl(player.getProfileUrl());
        metaEmbed.setTitle(player.getName() + "-" + player.getRealm() + " (" + current + ")");
        metaEmbed.setDescription(player.getActiveSpecName() + " " + player.getCharacterClass() + "(" + player.getActiveSpecRole() + ")"
                + "\n" + player.getRace() + " " + player.getFaction() + " " + player.getFaction());
        metaEmbed.setColor(4443459);

        DiscordWebhook.Thumbnail thumbnail = new DiscordWebhook.Thumbnail();
        thumbnail.setUrl(player.getThumbnailUrl());
        metaEmbed.setThumbnail(thumbnail);

        final var dungeons = player.getMythicPlusHighestLevelRuns();
        final double level = dungeons.stream().mapToDouble(MythicPlusDungeon::getMythicLevel).average().orElse(0);
        final double upgrade = dungeons.stream().mapToDouble(MythicPlusDungeon::getNumKeystoneUpgrades).average().orElse(0);
        final double clearTime = dungeons.stream().mapToDouble(MythicPlusDungeon::getClearTimeMs).average().orElse(0);
        final String tendency = dungeons.stream().mapToDouble(MythicPlusDungeon::getNumKeystoneUpgrades).average().orElse(0) > 0.5 ? "Timed ▲" : "Untimed ▼";


        final double prev = player.getMythicPlusScoresBySeason()
                .stream().filter(getFilter(Season.getPreviousSeasonKey()))
                .findFirst()
                .orElseGet(MythicPlusScore::new)
                .getAll();

        final String mythicPlusSummary = "Current score: " + current
                + "\nPrevious score: " + prev
                + "\nAverage level: " + level
                + "\nAverage upgrade: " + upgrade
                + "\nAverage clear time: " + MathUtils.msToString(clearTime)
                + "\nTendency: " + tendency;

        DiscordWebhook.Field mythicPlusField = new DiscordWebhook.Field();
        mythicPlusField.setName("Mythic Plus");
        mythicPlusField.setValue(mythicPlusSummary);
        mythicPlusField.setInline(true);

        final String raidSummary = "Vault of the Incarnates"
                + "\nMythic " + player.getRaidProgression().getVaultOfTheIncarnates().getMythicBossesKilled() + "/" + player.getRaidProgression().getVaultOfTheIncarnates().getTotalBosses()
                + "\nHeroic " + player.getRaidProgression().getVaultOfTheIncarnates().getHeroicBossesKilled() + "/" + player.getRaidProgression().getVaultOfTheIncarnates().getTotalBosses()
                + "\nNormal " + player.getRaidProgression().getVaultOfTheIncarnates().getNormalBossesKilled() + "/" + player.getRaidProgression().getVaultOfTheIncarnates().getTotalBosses();


        DiscordWebhook.Field raidField = new DiscordWebhook.Field();
        raidField.setName("Raid Progression");
        raidField.setValue(raidSummary);
        raidField.setInline(true);

        metaEmbed.setFields(new ArrayList<>());
        metaEmbed.getFields().addAll(List.of(mythicPlusField, raidField));

        DiscordWebhook.Footer footer = new DiscordWebhook.Footer();
        footer.setText("Gracefully delivered by WarcraftTools\nPlayer last updated: " + player.getLastCrawledAt());
        metaEmbed.setFooter(footer);

        data.setEmbeds(List.of(metaEmbed));

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
