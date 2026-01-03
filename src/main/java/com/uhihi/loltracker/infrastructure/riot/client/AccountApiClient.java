package com.uhihi.loltracker.infrastructure.riot.client;

import com.uhihi.loltracker.infrastructure.riot.config.RiotApiProperties;
import com.uhihi.loltracker.infrastructure.riot.dto.account.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AccountApiClient extends BaseRiotApiClient {

    public AccountApiClient(WebClient webClient, RiotApiProperties properties) {
        super(webClient, properties);
    }

    /**
     * Riot ID로 계정 정보 조회
     *
     * @param gameName Riot ID의 게임 이름 (예: "Hide on bush")
     * @param tagLine Riot ID의 태그라인 (예: "KR1")
     * @param region 지역 (asia, americas, europe, sea)
     * @return AccountDto
     */
    public Mono<AccountDto> getByRiotId(String gameName, String tagLine, String region) {
        String url = String.format("%s/riot/account/v1/accounts/by-riot-id/%s/%s",
                getRegionUrl(region),
                gameName,
                tagLine
        );

        log.info("Fetching account by Riot ID: {}#{} (region: {})", gameName, tagLine, region);
        return get(url, AccountDto.class);
    }

    /**
     * PUUID로 계정 정보 조회
     *
     * @param puuid PUUID
     * @param region 지역
     * @return AccountDto
     */
    public Mono<AccountDto> getByPuuid(String puuid, String region) {
        String url = String.format("%s/riot/account/v1/accounts/by-puuid/%s",
                getRegionUrl(region),
                puuid
        );

        log.info("Fetching account by PUUID: {} (region: {})", puuid, region);
        return get(url, AccountDto.class);
    }
}