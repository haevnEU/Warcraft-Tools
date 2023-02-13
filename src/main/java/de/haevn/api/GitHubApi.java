package de.haevn.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.flogger.FluentLogger;
import de.haevn.exceptions.NetworkException;
import de.haevn.model.CountryRealm;
import de.haevn.model.rating.MythicPlusScoreMapping;
import de.haevn.model.rating.RatingDefinition;
import de.haevn.model.weekly.Affix;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.Network;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class GitHubApi extends AbstractApi {

    private static final String SEASONAL_KEY = "git.url.seasonal";
    private static final String DEFINITION_KEY = "git.url.definition";
    private static final String SCORE_MAP_KEY = "git.url.scoreMap";
    private static final String AFFIX_COMBO_KEY = "git.url.affixCombo";
    private static final String REALM_KEY = "git.url.realm";
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
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
            LOGGER.atWarning().log("Failed to update. Amount failed endpoints %s", result);
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
        LOGGER.atFine().log("Updating GitHubApi.");
        if ((lastUpdate.get() + refreshDuration) < System.currentTimeMillis()) {
            lastUpdate.set(System.currentTimeMillis());
            lastUpdateDate.set(Date.from(Instant.now()));
            fetchSeasonalDungeons().thenAccept(resultConsumer);
            fetchRatingDefinition().thenAccept(resultConsumer);
            fetchMythicPlusScoreMapping().thenAccept(resultConsumer);
            fetchAffixRotation().thenAccept(resultConsumer);
            fetchCountryRealms().thenAccept(resultConsumer);
            LOGGER.atFine().log("GitHubApi updated.");
        } else {
            LOGGER.atFine().log("GitHubApi is already up to date.");
        }
    }

    private CompletableFuture<Integer> fetchSeasonalDungeons() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = urlHandler.get(SEASONAL_KEY);

                LOGGER.atFine().log("Fetching seasonal dungeons from %s.", url);
                final HttpResponse<String> result = Network.download(url);
                LOGGER.atFine().log("Request result: %s %s bytes", result.statusCode(), result.body().length());

                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }

                LOGGER.atFine().log("Parsing json string.");
                final String json = result.body();
                final Map<String, String> temporaryMap = JsonAndStringUtils.parse(json, new TypeReference<Map<String, String>>() {
                });
                LOGGER.atFine().log("Parsing done.");

                dungeons.set(temporaryMap);
                return 0;
            } catch (Exception ex) {
                LOGGER.atWarning().withCause(ex).log("Failed to fetch seasonal dungeons.");
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchRatingDefinition() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = urlHandler.get(DEFINITION_KEY);

                LOGGER.atFine().log("Fetching rating definition from %s.", url);
                final HttpResponse<String> result = Network.download(url);
                LOGGER.atFine().log("Request result: %s %s bytes", result.statusCode(), result.body().length());
                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }

                LOGGER.atFine().log("Parsing json string.");
                final String json = result.body();
                final RatingDefinition suggestion = JsonAndStringUtils.parse(json, RatingDefinition.class);
                LOGGER.atFine().log("Parsing done.");

                ratingDefinition.set(suggestion);
                return 0;
            } catch (Exception ex) {
                LOGGER.atWarning().withCause(ex).log("Failed to fetch rating definition.");
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchMythicPlusScoreMapping() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = urlHandler.get(SCORE_MAP_KEY);

                LOGGER.atFine().log("Fetching score mapping from %s.", url);
                final HttpResponse<String> result = Network.download(url);
                LOGGER.atFine().log("Request result: %s %s bytes", result.statusCode(), result.body().length());

                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }

                LOGGER.atFine().log("Parsing json string.");
                final String json = result.body();
                final MythicPlusScoreMapping mapping = JsonAndStringUtils.parse(json, MythicPlusScoreMapping.class);
                LOGGER.atFine().log("Parsing done.");

                mythicPlusScoreMappingProperty.set(mapping);
                return 0;
            } catch (Exception ex) {
                LOGGER.atWarning().withCause(ex).log("Failed to fetch score mapping.");
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchAffixRotation() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = urlHandler.get(AFFIX_COMBO_KEY);

                LOGGER.atFine().log("Fetching affix rotation from %s.", url);
                final HttpResponse<String> result = Network.download(url);
                LOGGER.atFine().log("Request result: %s %s bytes", result.statusCode(), result.body().length());

                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }

                LOGGER.atFine().log("Parsing json string.");
                final String json = result.body();
                final Map<String, List<Affix>> rotation = new HashMap<>();
                final JsonNode rootNode = JsonAndStringUtils.parse(json, JsonNode.class);
                final JsonNode node = rootNode.get("affix_combos");
                LOGGER.atFine().log("Create affix rotation.");
                node.forEach(jsonNode -> {
                    final List<Affix> affixList = new ArrayList<>();
                    JsonAndStringUtils.parseSecure(jsonNode.get("first").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("second").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("third").toString(), Affix.class).ifPresent(affixList::add);
                    JsonAndStringUtils.parseSecure(jsonNode.get("fourth").toString(), Affix.class).ifPresent(affixList::add);
                    rotation.put(jsonNode.get("id").toString(), affixList);
                });
                LOGGER.atFine().log("Parsing done.");

                affixRotation.set(rotation);
                return 0;
            } catch (Exception ex) {
                LOGGER.atWarning().withCause(ex).log("Failed to fetch affix rotation.");
                return -1;
            }
        });
    }

    private CompletableFuture<Integer> fetchCountryRealms() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final String url = urlHandler.get(REALM_KEY);

                LOGGER.atFine().log("Fetching country realms from %s.", url);
                final HttpResponse<String> result = Network.download(url);
                LOGGER.atFine().log("Request result: %s %s bytes", result.statusCode(), result.body().length());

                if (!Network.is2xx(result.statusCode())) {
                    throw new NetworkException(result);
                }

                LOGGER.atFine().log("Parsing json string.");
                final String json = result.body();
                final CountryRealm countryRealm = JsonAndStringUtils.parse(json, CountryRealm.class);
                LOGGER.atFine().log("Parsing done.");

                countryRealms.set(countryRealm);
                return 0;
            } catch (Exception ex) {
                LOGGER.atWarning().withCause(ex).log("Failed to fetch country realms.");
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

}
