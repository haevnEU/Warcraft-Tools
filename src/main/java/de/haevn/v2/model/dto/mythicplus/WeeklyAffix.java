package de.haevn.v2.model.dto.mythicplus;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class WeeklyAffix {
    private String region;
    private String title;

    @JsonAlias("leaderboard_url")
    private String leaderboardUrl;

    private Affix[] affix_details;
}
