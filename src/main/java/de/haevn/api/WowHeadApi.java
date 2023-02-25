package de.haevn.api;

import de.haevn.logging.Logger;
import de.haevn.logging.LoggerHandler;
import de.haevn.utils.NetworkUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;

public class WowHeadApi extends AbstractApi {
    private static final Logger LOGGER = LoggerHandler.get(WowHeadApi.class);
    private static final WowHeadApi INSTANCE = new WowHeadApi();

    private WowHeadApi() {
    }

    public static WowHeadApi getInstance() {
        return INSTANCE;
    }

    public CompletableFuture<String> getNameForSpell(int spellID) {
        String url = "http://www.wowdb.com/spells/" + spellID;

        return NetworkUtils.downloadAsync(url).thenApply(responseOpt -> {
            if (responseOpt.isEmpty()) return "";
            var response = responseOpt.get();
            if (NetworkUtils.isNot2xx(response.statusCode())) {
                LOGGER.atInfo("Got response for spell %s, Code %s", spellID, response.statusCode());
                return "";
            }

            String spell = StringUtils.substringBetween(response.body(), "<title>", "</title>");
            spell = spell.replace(" - Spells - WoWDB ", "");
            return spell;
        });
    }

}
