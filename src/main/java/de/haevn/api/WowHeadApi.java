package de.haevn.api;

import com.google.common.flogger.FluentLogger;
import de.haevn.utils.Network;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;

public class WowHeadApi extends AbstractApi {
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
    private static final WowHeadApi INSTANCE = new WowHeadApi();

    public static WowHeadApi getInstance() {
        return INSTANCE;
    }

    private WowHeadApi() {
    }

    public CompletableFuture<String> getNameForSpell(int spellID) {
        String url = "http://www.wowdb.com/spells/" + spellID;

        return Network.downloadAsync(url).thenApply(responseOpt -> {
            if (responseOpt.isEmpty()) return "";
            var response = responseOpt.get();
            if (!Network.is2xx(response.statusCode())) {
                LOGGER.atInfo().log("Got response for spell %s, Code %s", spellID, response.statusCode());
                return "";
            }

            String spell = StringUtils.substringBetween(response.body(), "<title>", "</title>");
            spell = spell.replace(" - Spells - WoWDB ", "");
            return spell;
        });
    }

}