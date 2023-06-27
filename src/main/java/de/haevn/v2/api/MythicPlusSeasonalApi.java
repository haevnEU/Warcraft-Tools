package de.haevn.v2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import de.haevn.v2.model.MythicPlusSeasonalData;
import de.haevn.v2.model.dto.mythicplus.SeasonCutoff;
import de.haevn.v2.model.dto.mythicplus.WeeklyAffix;
import de.haevn.v2.model.mapper.MythicPlusSeasonalDataMapper;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MythicPlusSeasonalApi implements IApi<MythicPlusSeasonalData> {
    private static final String CURRENT_WEEKLY_AFFIX_URL = String.format("https://raider.io/api/v1/mythic-plus/affixes?region=%s&locale=%s", "eu", "en");
    private static final String SEASON_CUTOFF_URL = String.format("https://raider.io/api/v1/mythic-plus/season-cutoffs?season=%s&region=%s", "season-df-2", "eu");

    MythicPlusSeasonalApi() {
    }

    public CompletableFuture<MythicPlusSeasonalData> refresh() {

        return CompletableFuture.supplyAsync(() -> {
            final MythicPlusSeasonalDataMapper mapper = new MythicPlusSeasonalDataMapper();

            final var currentWeek = requestCurrentWeeklyAffix().join();
            final var nextWeek = requestCurrentWeeklyAffix().join();
            final var seasonCutoff = requestSeasonCutoff().join();

            currentWeek.ifPresent(mapper::setCurrentWeeklyAffix);
            nextWeek.ifPresent(mapper::setNextWeeklyAffix);
            seasonCutoff.ifPresent(mapper::setSeasonCutoff);

            return mapper.build();
        });
    }

    public CompletableFuture<Optional<SeasonCutoff>> requestSeasonCutoff() {
        return get(SEASON_CUTOFF_URL, new TypeReference<SeasonCutoff>() {
        }, "cutoffs");
    }

    public CompletableFuture<Optional<WeeklyAffix>> requestCurrentWeeklyAffix() {
        return get(CURRENT_WEEKLY_AFFIX_URL, new TypeReference<WeeklyAffix>() {
        });
    }

}
