package com.uhihi.loltracker.infrastructure.riot.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantDto {

    // Player Identity
    private String puuid;
    private String riotIdGameName;
    private String riotIdTagline;
    private String summonerId;
    private String summonerName;
    private int summonerLevel;

    // Champion
    private int championId;
    private String championName;
    private int champLevel;

    // Team & Position
    private int teamId;
    private String teamPosition;
    private String individualPosition;
    private String role;
    private String lane;

    // Game Result
    private boolean win;
    private boolean gameEndedInEarlySurrender;
    private boolean gameEndedInSurrender;
    private boolean teamEarlySurrendered;

    // KDA
    private int kills;
    private int deaths;
    private int assists;
    private double kda;

    // Damage
    private int totalDamageDealtToChampions;
    private int totalDamageTaken;
    private int damageDealtToObjectives;
    private int damageDealtToTurrets;
    private int magicDamageDealtToChampions;
    private int physicalDamageDealtToChampions;
    private int trueDamageDealtToChampions;

    // Gold & CS
    private int goldEarned;
    private int goldSpent;
    private int totalMinionsKilled;
    private int neutralMinionsKilled;

    // Vision
    private int visionScore;
    private int wardsPlaced;
    private int wardsKilled;
    private int detectorWardsPlaced;

    // Items
    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;

    // Summoner Spells
    private int summoner1Id;
    private int summoner2Id;

    // Perks (Runes)
    private PerksDto perks;

    // Objectives
    private int turretKills;
    private int inhibitorKills;
    private int baronKills;
    private int dragonKills;
    private int nexusKills;

    // Combat
    private int totalHeal;
    private int totalHealsOnTeammates;
    private int totalUnitsHealed;
    private int longestTimeSpentLiving;
    private int largestKillingSpree;
    private int largestMultiKill;
    private int doubleKills;
    private int tripleKills;
    private int quadraKills;
    private int pentaKills;

    // Other
    private int timePlayed;
    private int timeCCingOthers;
    private boolean firstBloodKill;
    private boolean firstTowerKill;
}