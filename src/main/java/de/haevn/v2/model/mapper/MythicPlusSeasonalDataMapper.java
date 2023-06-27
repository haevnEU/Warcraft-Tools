package de.haevn.v2.model.mapper;

import de.haevn.v2.model.MythicPlusSeasonalData;
import de.haevn.v2.model.dto.mythicplus.Cutoff;
import de.haevn.v2.model.dto.mythicplus.SeasonCutoff;
import de.haevn.v2.model.dto.mythicplus.WeeklyAffix;

public class MythicPlusSeasonalDataMapper {
    private final MythicPlusSeasonalData raiderIoData = new MythicPlusSeasonalData();
    public MythicPlusSeasonalDataMapper() {
        super();
    }

    public MythicPlusSeasonalDataMapper setCurrentWeeklyAffix(WeeklyAffix currentWeeklyAffix) {
        raiderIoData.setCurrentWeek(currentWeeklyAffix);
        return this;
    }

    public MythicPlusSeasonalDataMapper setNextWeeklyAffix(WeeklyAffix nextWeeklyAffix) {
        raiderIoData.setNextWeek(nextWeeklyAffix);
        return this;
    }

    private double extractScore(Cutoff cutoff) {
        if (cutoff == null) {
            return 0;
        }
        return cutoff.getAll().getQuantileMinValue();
    }

    public MythicPlusSeasonalDataMapper setSeasonCutoff(SeasonCutoff seasonCutoff) {
        seasonCutoff.getP999().setScore(extractScore(seasonCutoff.getP999()));
        seasonCutoff.getP990().setScore(extractScore(seasonCutoff.getP990()));
        seasonCutoff.getP900().setScore(extractScore(seasonCutoff.getP900()));
        seasonCutoff.getP750().setScore(extractScore(seasonCutoff.getP750()));
        seasonCutoff.getP600().setScore(extractScore(seasonCutoff.getP600()));

        raiderIoData.setSeasonCutoff(seasonCutoff);
        return this;
    }

    public MythicPlusSeasonalData build() {
        return raiderIoData;
    }
}
