package de.haevn.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import de.haevn.enumeration.Addon;
import de.haevn.enumeration.Season;
import de.haevn.exceptions.NetworkException;
import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.model.seasonal.SeasonCutoff;
import de.haevn.model.weekly.Affix;
import de.haevn.utils.SerializationUtils;
import de.haevn.utils.NetworkUtils;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class RaiderIOApi extends AbstractApi {
    private static final Logger LOGGER = LoggerHandler.get(RaiderIOApi.class);

    private static final RaiderIOApi INSTANCE = new RaiderIOApi();
    private static final String CURRENT_AFFIX_KEY = "rio.url.currentAffix";
    private static final String CUTOFF_KEY = "rio.url.cutoff";
    private static final String CHARACTER_KEY = "rio.url.character";
    private static final String QUERY_KEY = "rio.query.character";
    private static final String QUERY_KEY_SEASONS = "rio.query.character.seasons";

    private final SimpleObjectProperty<Date> lastUpdateDateTime = new SimpleObjectProperty<>(new Date());
    private final SimpleObjectProperty<List<Affix>> currentAffix = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Map<Addon, List<SeasonCutoff>>> cutoffs = new SimpleObjectProperty<>(new EnumMap<>(Addon.class));
    private final SimpleObjectProperty<SeasonCutoff> currentCutoff = new SimpleObjectProperty<>();
    private long lastUpdate = 0;
    private final Consumer<Integer> resultConsumer = result -> {
        if (result < 0) {
            LOGGER.atWarning("Failed to update. Amount failed endpoints %s", result);
            lastUpdate = -1;
            lastUpdateDateTime.set(Date.from(Instant.EPOCH.minusSeconds(1)));
        }
    };

    private RaiderIOApi() {
    }

    public static RaiderIOApi getInstance() {
        return INSTANCE;
    }

    public void update() {
        LOGGER.atInfo("Updating RaiderIOApi");
        if ((lastUpdate + super.refreshDuration) - System.currentTimeMillis() > 0) {
            LOGGER.atInfo("GitHubApi is already up to date.");
            return;
        }
        lastUpdate = System.currentTimeMillis();
        lastUpdateDateTime.set(Date.from(Instant.now()));
        fetchCurrentAffix().thenAccept(resultConsumer);
        fetchAddons().thenAccept(resultConsumer);
        LOGGER.atInfo("Updating RaiderIOApi finished");
    }

    private CompletableFuture<Integer> fetchCurrentAffix() {
        final String url = String.format(urlHandler.get(CURRENT_AFFIX_KEY), region.get());
        LOGGER.atInfo("Fetching current affix from %s", url);
        return NetworkUtils.downloadAsync(url).thenApply(response -> {
            LOGGER.atInfo("Fetching current affix");
            if (response.isPresent()) {
                try {
                    final HttpResponse<String> result = response.get();
                    LOGGER.atInfo("Request result: %s %s bytes", result.statusCode(), result.body().length());
                    if (result.statusCode() != 200) {
                        throw new NetworkException(result);
                    }

                    LOGGER.atInfo("Parsing current affix");
                    final String json = result.body();
                    final JsonNode rootNode = SerializationUtils.parseJson(json, JsonNode.class);
                    LOGGER.atInfo("Transform root node into affix details");
                    final JsonNode node = rootNode.get("affix_details");
                    final List<Affix> currentRotation = SerializationUtils.parseJson(node.toString(), new TypeReference<List<Affix>>() {
                    });
                    LOGGER.atInfo("Parsing done.");

                    currentAffix.set(currentRotation);
                    return 0;
                } catch (JsonProcessingException | NetworkException e) {
                    LOGGER.atWarning("Failed to parse current affix");
                    return -1;
                }
            } else {
                LOGGER.atWarning("Failed to fetch current affix");
            }
            return -1;
        });
    }

    private CompletableFuture<Integer> fetchAddons() {

        return CompletableFuture.supplyAsync(() -> {
            LOGGER.atInfo("Fetching addons");
            final Map<Addon, List<SeasonCutoff>> cutoffMap = new EnumMap<>(Addon.class);
            Addon.getSeasons.forEach((addon, seasons) -> {
                LOGGER.atInfo("Fetching cutoff for %s", addon);
                final List<SeasonCutoff> seasonCutoffs = fetchCutoff(seasons);
                if (!seasonCutoffs.isEmpty()) {
                    cutoffMap.put(addon, seasonCutoffs);
                    LOGGER.atInfo("Fetched cutoff for %s", addon);
                } else {
                    LOGGER.atInfo("Failed to fetch cutoff for %s", addon);
                }
            });

            if (cutoffMap.size() > 0) {
                LOGGER.atInfo("Fetched cutoff for all addons");
                currentCutoff.set(cutoffMap.get(Addon.DRAGONFLIGHT).stream().filter(SeasonCutoff::isCurrent).findFirst().orElse(null));
                cutoffs.set(cutoffMap);
            } else {
                LOGGER.atInfo("Failed to fetch cutoff for all addons");
            }

            return 0;
        });
    }

    private List<SeasonCutoff> fetchCutoff(Season... seasons) {
        final List<SeasonCutoff> seasonCutoffs = new ArrayList<>();
        for (var season : seasons) {
            try {
                final String slug = season.label;
                final String url = String.format(urlHandler.get(CUTOFF_KEY), slug, region.get());
                LOGGER.atInfo("Fetching cutoff for season %s from %s", season.label, url);
                final HttpResponse<String> result = NetworkUtils.download(url);
                LOGGER.atInfo("Request result: %s %s bytes", result.statusCode(), result.body().length());
                final String json = result.body();

                LOGGER.atInfo("Parsing cutoff for season %s", season.label);
                final JsonNode rootNode = SerializationUtils.parseJson(json, JsonNode.class);
                LOGGER.atInfo("Transform root node into cutoff details");
                final JsonNode cutoffNode = rootNode.get("cutoffs");
                final SeasonCutoff cutoff = SerializationUtils.parseJson(cutoffNode.toString(), SeasonCutoff.class);
                cutoff.setSeasonKey(season.label);

                LOGGER.atInfo("Parsing done.");
                seasonCutoffs.add(cutoff);
            } catch (Exception e) {
                LOGGER.atWarning("Failed to parse cutoff for season %s", season.label);
            }
        }
        return seasonCutoffs;
    }

    public CompletableFuture<NetworkData<PlayerLookupModel>> searchPlayerAsync(String realm, String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.atInfo("Fetching player %s-%s", realm, name);
                final String url = String.format(urlHandler.get(CHARACTER_KEY), region.get(), realm, name, (urlHandler.get(QUERY_KEY) + urlHandler.get(QUERY_KEY_SEASONS)));

                final HttpResponse<String> download = NetworkUtils.download(url);
                LOGGER.atInfo("Request result: %s %s bytes", download.statusCode(), download.body().length());
                if (!NetworkUtils.is2xx(download.statusCode())) {
                    throw new NetworkException(download);
                }

                LOGGER.atInfo("Parsing player %s-%s", realm, name);
                String json = download.body();
                var player = SerializationUtils.parseJson(json, PlayerLookupModel.class);

                LOGGER.atInfo("Parsing done.");
                return new NetworkData<>(player);

            } catch (NetworkException | JsonProcessingException e) {
                LOGGER.atWarning("Failed to parse player %s-%s", realm, name);
                return new NetworkData<>(null, e);
            }
        });
    }


    //----------------------------------------------------------------------------------------------------------------------
    //  Getter
    //----------------------------------------------------------------------------------------------------------------------


    public ReadOnlyObjectProperty<List<Affix>> getCurrentAffix() {
        return currentAffix;
    }

    public ReadOnlyObjectProperty<Map<Addon, List<SeasonCutoff>>> getCutoffs() {
        return cutoffs;
    }

    public ReadOnlyObjectProperty<SeasonCutoff> getCurrentCutoff() {
        return currentCutoff;
    }

    public ReadOnlyObjectProperty<Date> getLastUpdate() {
        return lastUpdateDateTime;
    }

}
