package de.haevn.v2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import de.haevn.v2.model.RaidData;
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

    private int step = 0;
    private int maxStep = 6;

    public CompletableFuture<RaidData> refresh() {
        return CompletableFuture.supplyAsync(() -> {
            RaidDataMapper mapper = new RaidDataMapper();

            final var hallOfFame = get(HALL_OF_FAME_URL, new TypeReference<Rank>() {
            }).join();

            Consumer<String> addRankings = slug -> {
                final var normalRanking = requestRanking(slug, "normal", region, realm).join();
                step++;
                final var heroicRanking = requestRanking(slug, "heroic", region, realm).join();
                step++;
                final var mythicRanking = requestRanking(slug, "mythic", region, realm).join();
                step++;

                normalRanking.ifPresent(r -> mapper.addNormalRaidRanking(slug, r));
                heroicRanking.ifPresent(r -> mapper.addHeroicRaidRanking(slug, r));
                mythicRanking.ifPresent(r -> mapper.addMythicRaidRanking(slug, r));

            };

            addRankings.accept(VaultOfTheIncarnates);
            addRankings.accept(aberrusTheShadowsCrucible);

            return mapper.build();
        });
    }

    public synchronized int currentStep() {
        return step;
    }

    public synchronized int maxStep() {
        return maxStep;
    }

    public CompletableFuture<Optional<Ranking>> requestRanking(String raidSlug, String mode, String region, String realm) {
        return get(String.format(RANKING_URL, raidSlug, mode, region, realm), new TypeReference<Ranking>() {
        });
    }


    @SneakyThrows
    public static void main(String[] args) {

        RaidApi api = new RaidApi();

        var call = api.refresh();
        int cnt = 0;
        int mult = 1;
        while (!call.isDone()) {
            System.out.print("\rApi request in execution (Step: " + api.currentStep() + "/" + api.maxStep + ") ");
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
        System.out.print("\r");
        var result = call.get();
        String guildName = "";
        do {
            Scanner in = new Scanner(System.in);
            guildName = in.nextLine();
            System.out.print("Enter guild name: ");
            var filtered = filterex(guildName, result);
            filtered.stream().map(rank -> "Guild: " + rank.getGuild().getName() + " (" + rank.getGuild().getFaction() + ") Realm: " + rank.getRank() + " Region: " + rank.getRegionRank())
                    .forEach(System.out::println);


        } while (!guildName.equalsIgnoreCase("!q"));
        System.out.println("Done");
    }

    static void print() {

    }

    static List<Rank> filterex(String guildName, RaidData result) {
        // Actual filtering for name or rank
        Predicate<Rank> filter = rank -> true;
        try {
            int desiredRank = Integer.parseInt(guildName);
            filter = rank -> rank.getRank() == desiredRank;
        } catch (NumberFormatException e) {
            String finalGuildName = guildName;
            filter = rank -> rank.getGuild().getName().toLowerCase().contains(finalGuildName.toLowerCase());
        }
        return result.getMythicRankings().get(VaultOfTheIncarnates).getRaidRankings().stream()
                .filter(filter).toList();
    }

}
