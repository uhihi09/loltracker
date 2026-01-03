package com.uhihi.loltracker.infrastructure.riot.client;

import com.uhihi.loltracker.infrastructure.riot.config.RiotApiProperties;
import com.uhihi.loltracker.infrastructure.riot.dto.summoner.SummonerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SummonerApiClient extends BaseRiotApiClient {

    public SummonerApiClient(WebClient webClient, RiotApiProperties properties) {
        super(webClient, properties);
    }

    /**
     * PUUID로 소환사 정보 조회
     *
     * @param puuid PUUID
     * @param platform 플랫폼 (kr, na1, euw1 등)
     * @return SummonerDto
     */
    public Mono<SummonerDto> getByPuuid(String puuid, String platform) {
        String url = String.format("https://%s.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/%s",
                platform,
                puuid
        );

        log.info("Fetching summoner by PUUID: {} (platform: {})", puuid, platform);
        return get(url, SummonerDto.class);
    }

    /**
     * Summoner ID로 소환사 정보 조회
     *
     * @param summonerId Summoner ID
     * @param platform 플랫폼
     * @return SummonerDto
     */
    public Mono<SummonerDto> getBySummonerId(String summonerId, String platform) {
        String url = String.format("https://%s.api.riotgames.com/lol/summoner/v4/summoners/%s",
                platform,
                summonerId
        );

        log.info("Fetching summoner by ID: {} (platform: {})", summonerId, platform);
        return get(url, SummonerDto.class);
    }
}