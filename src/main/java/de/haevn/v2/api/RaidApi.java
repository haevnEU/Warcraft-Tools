package de.haevn.v2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import de.haevn.v2.model.Addon;
import de.haevn.v2.model.raid.RaidData;
import de.haevn.v2.model.dto.raid.Raid;
import de.haevn.v2.model.dto.raid.Rank;
import de.haevn.v2.model.dto.raid.Ranking;
import de.haevn.v2.model.mapper.RaidDataMapper;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RaidApi implements IApi<RaidData> {

    private static final String region = "eu";
    private static final String aberrusTheShadowsCrucible = "aberrus-the-shadowed-crucible";
    private static final String VaultOfTheIncarnates = "vault-of-the-incarnates";
    private static final String mode = "mythic";
    private static final String realm = "eredar";
    private static final String limit = "100";
    private static final String page = "0";
    private static final String HALL_OF_FAME_URL = String.format("https://raider.io/api/v1/raiding/hall-of-fame?raid=%s&difficulty=%s&region=%s", aberrusTheShadowsCrucible, mode, region);
    private static final String RANKING_URL = "https://raider.io/api/v1/raiding/raid-rankings?raid=%s&difficulty=%s&region=%s&realm=%s&limit=100&page=0";
    private static final String STATIC_DATA_URL = "https://raider.io/api/v1/raiding/static-data?expansion_id=%s";



    public CompletableFuture<RaidData> refresh() {
        return CompletableFuture.supplyAsync(() -> {
            Addon addon = new Addon();
            // Request information about the current addon

            var addonStaticData = get(String.format(STATIC_DATA_URL, 9), new TypeReference<List<Raid>>() {}, "raids").join();
            if(addonStaticData.isEmpty()){
                System.out.println("No addon data found");
                return null;
            }
            for (Raid raid : addonStaticData.get()) {
                
                // Request addon
                addon.addRaid(raid.getSlug(), raid);
            }
            return null;
        });
    }











    @SneakyThrows
    public static void main(String[] args) {

        RaidApi api = new RaidApi();

        var call = api.refresh();
        int cnt = 0;
        int mult = 1;
        while (!call.isDone()) {
            System.out.print("\rApi request in execution (Step: "  + "/" + api.maxStep + ") ");
            for (int i = 0; i < 20; i++) {
                if (i == cnt) {
                    System.out.print("█");
                } else {
                    System.out.print("░");
                }
            }
            if (cnt < 0) mult = 1;
            else if (cnt >= 19) mult = -1;
            cnt = cnt + mult;
            Thread.sleep(125);
        }

    }


    static List<Rank> filterex(String guildName, RaidData result, String slug) {
        // Actual filtering for name or rank
        Predicate<Rank> filter = rank -> true;
        try {
            int desiredRank = Integer.parseInt(guildName);
            filter = rank -> rank.getRank() == desiredRank;
        } catch (NumberFormatException e) {
            String finalGuildName = guildName;
            filter = rank -> rank.getGuild().getName().toLowerCase().contains(finalGuildName.toLowerCase());
        }
        return result.getMythicRankings().get(slug).getRaidRankings().stream()
                .filter(filter).toList();
    }

}
