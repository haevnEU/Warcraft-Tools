package de.haevn.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import de.haevn.enumeration.Addon;
import de.haevn.enumeration.Season;
import de.haevn.exceptions.NetworkException;
import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.model.seasonal.SeasonCutoff;
import de.haevn.model.weekly.Affix;
import de.haevn.utils.ExceptionUtils;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.Network;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class RaiderIOApi extends AbstractApi {
    private static final Logger LOGGER = Logger.getLogger(RaiderIOApi.class.getName());


    private static final String CURRENT_AFFIX_KEY = "rio.url.currentAffix";
    private static final String STATIC_DATA_KEY = "rio.url.staticData";
    private static final String CUTOFF_KEY = "rio.url.cutoff";
    private static final String CHARACTER_KEY = "rio.url.character";
    private static final String QUERY_KEY = "rio.query.character";
    private static final String QUERY_KEY_SEASONS = "rio.query.character.seasons";
    private static final RaiderIOApi instance = new RaiderIOApi();
    private final SimpleObjectProperty<Date> lastUpdateDateTime = new SimpleObjectProperty<>(new Date());
    private final SimpleObjectProperty<List<Affix>> currentAffix = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Map<Addon, List<SeasonCutoff>>> cutoffs = new SimpleObjectProperty<>(new EnumMap<>(Addon.class));
    private final SimpleObjectProperty<SeasonCutoff> currentCutoff = new SimpleObjectProperty<>();
    private long lastUpdate = 0;
    private final Consumer<Integer> resultConsumer = result -> {
        if (result < 0) {
            lastUpdate = -1;
            lastUpdateDateTime.set(Date.from(Instant.EPOCH.minusSeconds(1)));
        }
    };
    private RaiderIOApi() {
    }

    public static RaiderIOApi getInstance() {
        return instance;
    }

    public void update() {
        if ((lastUpdate + super.refreshDuration) - System.currentTimeMillis() > 0) {
            return;
        }
        lastUpdate = System.currentTimeMillis();
        lastUpdateDateTime.set(Date.from(Instant.now()));
        fetchCurrentAffix().thenAccept(resultConsumer);
        fetchAddons().thenAccept(resultConsumer);

    }

    private CompletableFuture<Integer> fetchCurrentAffix() {
        String url = String.format(urlHandler.get(CURRENT_AFFIX_KEY), region.get());
        return Network.downloadAsync(url).thenApply(response -> {
            if (response.isPresent()) {
                try {
                    if (response.get().statusCode() != 200) {
                        throw new NetworkException(response.get());
                    }
                    String json = response.get().body();
                    JsonNode rootNode = JsonAndStringUtils.parse(json, JsonNode.class);
                    JsonNode node = rootNode.get("affix_details");
                    var currentRotation = JsonAndStringUtils.parse(node.toString(), new TypeReference<List<Affix>>() {
                    });
                    currentAffix.set(currentRotation);
                    return 0;
                } catch (JsonProcessingException | NetworkException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
            return -1;
        });
    }

    private CompletableFuture<Integer> fetchAddons() {

        return CompletableFuture.supplyAsync(() -> {
            Map<Addon, List<SeasonCutoff>> cutoffMap = new EnumMap<>(Addon.class);
            Addon.getSeasons.forEach((addon, seasons) -> {
                var seasonCutoffs = fetchCutoff(seasons);
                if (!seasonCutoffs.isEmpty()) {
                    cutoffMap.put(addon, seasonCutoffs);
                }
            });

            if (cutoffMap.size() > 0) {
                currentCutoff.set(cutoffMap.get(Addon.DRAGONFLIGHT).stream().filter(SeasonCutoff::isCurrent).findFirst().orElse(null));
                cutoffs.set(cutoffMap);
            }

            return 0;
        });
    }

    private List<SeasonCutoff> fetchCutoff(Season... seasons) {
        List<SeasonCutoff> seasonCutoffs = new ArrayList<>();
        for (var season : seasons) {
            try {
                String slug = season.label;
                String url = String.format(urlHandler.get(CUTOFF_KEY), slug, region.get());
                String json = Network.download(url).body();
                JsonNode rootNode = JsonAndStringUtils.parse(json, JsonNode.class);
                JsonNode cutoffNode = rootNode.get("cutoffs");
                var cutoff = JsonAndStringUtils.parse(cutoffNode.toString(), SeasonCutoff.class);
                cutoff.setSeasonKey(season.label);
                seasonCutoffs.add(cutoff);
            } catch (Exception ex) {
                LOGGER.warning(ExceptionUtils.getStackTrace(ex));
            }
        }
        return seasonCutoffs;
    }

    public CompletableFuture<NetworkData<PlayerLookupModel>> searchPlayerAsync(String realm, String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format(urlHandler.get(CHARACTER_KEY), region.get(), realm, name, (urlHandler.get(QUERY_KEY) + urlHandler.get(QUERY_KEY_SEASONS)));
                var download = Network.download(url);
                if (!Network.is2xx(download.statusCode())) {
                    throw new NetworkException(download);
                }
                String json = download.body();
                var player = JsonAndStringUtils.parse(json, PlayerLookupModel.class);

                return new NetworkData<>(player);

            } catch (NetworkException | JsonProcessingException e) {
                LOGGER.warning(ExceptionUtils.getStackTrace(e));
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
