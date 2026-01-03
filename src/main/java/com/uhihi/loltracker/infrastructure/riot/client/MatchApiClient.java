package com.uhihi.loltracker.infrastructure.riot.client;

import com.uhihi.loltracker.infrastructure.riot.config.RiotApiProperties;
import com.uhihi.loltracker.infrastructure.riot.dto.match.MatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class MatchApiClient extends BaseRiotApiClient {

    public MatchApiClient(WebClient webClient, RiotApiProperties properties) {
        super(webClient, properties);
    }

    /**
     * PUUID로 매치 ID 리스트 조회
     *
     * @param puuid PUUID
     * @param region 지역 (asia, americas, europe, sea)
     * @param count 가져올 매치 수 (기본 20, 최대 100)
     * @param queue 큐 타입 (0 = 커스텀)
     * @return 매치 ID 리스트
     */
    public Mono<List<String>> getMatchIdsByPuuid(String puuid, String region, Integer count, Integer queue) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(String.format("%s/lol/match/v5/matches/by-puuid/%s/ids",
                        getRegionUrl(region),
                        puuid
                ));

        if (count != null) {
            builder.queryParam("count", count);
        }
        if (queue != null) {
            builder.queryParam("queue", queue);
        }

        String url = builder.toUriString();

        log.info("Fetching match IDs by PUUID: {} (region: {}, count: {}, queue: {})",
                puuid, region, count, queue);

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleError(response))
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .retryWhen(createRetrySpec(url))
                .doOnSuccess(matchIds -> log.debug("Found {} match IDs", matchIds.size()))
                .doOnError(error -> log.error("Failed to fetch match IDs", error));
    }

    /**
     * 매치 ID로 매치 상세 정보 조회
     *
     * @param matchId 매치 ID
     * @param region 지역
     * @return MatchDto
     */
    public Mono<MatchDto> getMatchById(String matchId, String region) {
        String url = String.format("%s/lol/match/v5/matches/%s",
                getRegionUrl(region),
                matchId
        );

        log.info("Fetching match detail: {} (region: {})", matchId, region);
        return get(url, MatchDto.class);
    }

    /**
     * 커스텀 게임 매치만 조회하는 편의 메서드
     */
    public Mono<List<String>> getCustomMatchIds(String puuid, String region, int count) {
        return getMatchIdsByPuuid(puuid, region, count, 0); // queue=0은 커스텀 게임
    }
}