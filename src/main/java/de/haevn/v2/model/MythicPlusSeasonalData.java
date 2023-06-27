package de.haevn.v2.model;

import de.haevn.v2.model.dto.mythicplus.SeasonCutoff;
import de.haevn.v2.model.dto.mythicplus.WeeklyAffix;
import lombok.Data;


@Data
public class MythicPlusSeasonalData {
    private WeeklyAffix currentWeek;
    private WeeklyAffix nextWeek;
    private SeasonCutoff seasonCutoff;
}
