package de.haevn.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import de.haevn.exceptions.NetworkException;
import de.haevn.model.CountryRealm;
import de.haevn.model.rating.MythicPlusScoreMapping;
import de.haevn.model.rating.RatingDefinition;
import de.haevn.model.weekly.Affix;
import de.haevn.utils.ExceptionUtils;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.Logger;
import de.haevn.utils.Network;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class GitHubApi extends AbstractApi {

    public static final String STYLESHEET_KEY = "git.url.stylesheet";
    private static final String SEASONAL_KEY = "git.url.seasonal";
    private static final String DEFINITION_KEY = "git.url.definition";
    private static final String SCORE_MAP_KEY = "git.url.scoreMap";
    private static final String AFFIX_COMBO_KEY = "git.url.affixCombo";
    private static final String REALM_KEY = "git.url.realm";

    private static final Logger LOGGER = new Logger(GitHubApi.class);
    private static final GitHubApi instance = new GitHubApi();
    private final SimpleLongProperty lastUpdate = new SimpleLongProperty(0);
    private final SimpleObjectProperty<Date> lastUpdateDate = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MythicPlusScoreMapping> mythicPlusScoreMappingProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RatingDefinition> ratingDefinition = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Map<String, String>> dungeons = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Map<String, List<Affix>>> affixRotation = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<CountryRealm> countryRealms = new SimpleObjectProperty<>();
    private final Consumer<Integer> resultConsumer = result -> {
        if (result < 0) {
            LOGGER.err("Failed to update " + result + " times.");
            lastUpdate.set(-1);
            lastUpdateDate.set(Date.from(Instant.EPOCH.minusSeconds(1)));
        }
    };

    private GitHubApi() {
        super();
    }

    public static GitHubApi getInstance() {
        return instance;
    }

    public void update() {
        if ((lastUpdate.get() + refreshDuration) < System.currentTimeMillis()) {
            lastUpdate.set(System.currentTimeMillis());
            lastUpdateDate.set(Date.from(Instant.now()));
            fetchSeasonalDungeons().thenAccept(resultConsumer);
            fetchRatingDefinition().thenAccept(resultConsumer);
            fetchMythicPlusScoreMapping().thenAccept(resultConsumer);
            fetchAffixRotation().thenAccept(resultConsumer);
            fetchCountryRealms().thenAccept(resultConsumer);
        }
    }

    private CompletableFuture<Integer> fetchSeasonalDungeons() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Network.download(super.urlHandler.get(SEASONAL_KEY));
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }
                String json = result.body();
                var temporaryMap = JsonAndStringUtils.parse(json, new TypeReference<Map<String, String>>() {
                });
                dungeons.set(temporaryMap);
                return 0;
            } catch (Exception ex) {
                LOGGER.err(ExceptionUtils.getStackTrace(ex));
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchRatingDefinition() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Network.download(super.urlHandler.get(DEFINITION_KEY));
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }
                String json = result.body();
                var suggestion = JsonAndStringUtils.parse(json, RatingDefinition.class);
                ratingDefinition.set(suggestion);
                return 0;
            } catch (Exception ex) {
                LOGGER.err(ExceptionUtils.getStackTrace(ex));
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchMythicPlusScoreMapping() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Network.download(super.urlHandler.get(SCORE_MAP_KEY));
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }
                String json = result.body();
                MythicPlusScoreMapping mapping = JsonAndStringUtils.parse(json, MythicPlusScoreMapping.class);
                mythicPlusScoreMappingProperty.set(mapping);
                return 0;
            } catch (Exception ex) {
                LOGGER.err(ExceptionUtils.getStackTrace(ex));
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchAffixRotation() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Network.download(super.urlHandler.get(AFFIX_COMBO_KEY));
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }
                String json = result.body();
                Map<String, List<Affix>> rotation = new HashMap<>();
                var rootNode = JsonAndStringUtils.parse(json, JsonNode.class);
                JsonNode node = rootNode.get("affix_combos");
                node.forEach(jsonNode -> {
                    List<Affix> affixList = new ArrayList<>();
                    JsonAndStringUtils.parseSecure(jsonNode.get("first").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("second").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("third").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("fourth").toString(), Affix.class).ifPresent(affixList::add);
                    rotation.put(jsonNode.get("id").toString(), affixList);
                });
                affixRotation.set(rotation);
                return 0;
            } catch (Exception ex) {
                LOGGER.err(ExceptionUtils.getStackTrace(ex));
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchCountryRealms() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var result = Network.download(super.urlHandler.get(REALM_KEY));
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }
                String json = result.body();
                var countryRealm = JsonAndStringUtils.parse(json, CountryRealm.class);
                countryRealms.set(countryRealm);
                return 0;
            } catch (Exception ex) {
                LOGGER.err(ExceptionUtils.getStackTrace(ex));
                return -1;
            }
        });
    }


    //----------------------------------------------------------------------------------------------------------------------
    // Getter
    //----------------------------------------------------------------------------------------------------------------------

    public ReadOnlyObjectProperty<MythicPlusScoreMapping> getMythicPlusScoreMappingProperty() {
        return mythicPlusScoreMappingProperty;
    }

    public ReadOnlyObjectProperty<RatingDefinition> getRatingDefinition() {
        return ratingDefinition;
    }

    public ReadOnlyObjectProperty<Map<String, String>> getDungeons() {
        return dungeons;
    }

    public ReadOnlyObjectProperty<Map<String, List<Affix>>> getAffixRotation() {
        return affixRotation;
    }

    public ReadOnlyObjectProperty<CountryRealm> getCountryRealms() {
        return countryRealms;
    }

    public ReadOnlyObjectProperty<Date> getLastUpdate() {
        return lastUpdateDate;
    }

    public String getStylesheetUrl() {
        return super.urlHandler.get(STYLESHEET_KEY);
    }
}
