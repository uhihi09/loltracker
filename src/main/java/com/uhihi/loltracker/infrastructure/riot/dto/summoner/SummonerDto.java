package com.uhihi.loltracker.infrastructure.riot.dto.summoner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummonerDto {

    private String id;              // 소환사 아이디
    private String accountId;       // 계정 아이디
    private String puuid;           // PUUID
    private String name;            // Summoner Name (deprecated)
    private int profileIconId;
    private long revisionDate;
    private long summonerLevel;
}