package com.uhihi.loltracker.infrastructure.riot.client;

import com.uhihi.loltracker.infrastructure.riot.dto.account.AccountDto;
import com.uhihi.loltracker.infrastructure.riot.dto.match.MatchDto;
import com.uhihi.loltracker.infrastructure.riot.dto.summoner.SummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Riot API 클라이언트 사용 예시
 * 실제 서비스에서는 이런 방식으로 사용하면 됩니다
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiotApiClientExample {

    private final AccountApiClient accountApiClient;
    private final SummonerApiClient summonerApiClient;
    private final MatchApiClient matchApiClient;

    /**
     * 예시 1: Riot ID로 플레이어 정보 조회
     */
    public Mono<AccountDto> getPlayerByRiotId(String gameName, String tagLine) {
        return accountApiClient.getByRiotId(gameName, tagLine, "asia")
                .doOnSuccess(account ->
                        log.info("Found player: {}#{} (PUUID: {})",
                                account.getGameName(),
                                account.getTagLine(),
                                account.getPuuid())
                );
    }

    /**
     * 예시 2: 플레이어의 최근 커스텀 게임 조회
     */
    public Mono<List<String>> getRecentCustomGames(String puuid) {
        return matchApiClient.getCustomMatchIds(puuid, "asia", 20)
                .doOnSuccess(matchIds ->
                        log.info("Found {} custom matches for PUUID: {}", matchIds.size(), puuid)
                );
    }

    /**
     * 예시 3: 매치 상세 정보 조회
     */
    public Mono<MatchDto> getMatchDetail(String matchId) {
        return matchApiClient.getMatchById(matchId, "asia")
                .doOnSuccess(match ->
                        log.info("Match {} - Duration: {}s, Participants: {}",
                                matchId,
                                match.getInfo().getGameDuration(),
                                match.getInfo().getParticipants().size())
                );
    }

    /**
     * 예시 4: 여러 플레이어의 최근 커스텀 게임을 모두 조회
     */
    public Flux<MatchDto> getCustomGamesForPlayers(List<String> puuids) {
        return Flux.fromIterable(puuids)
                .flatMap(puuid -> matchApiClient.getCustomMatchIds(puuid, "asia", 20))
                .flatMapIterable(matchIds -> matchIds)
                .distinct() // 중복 제거
                .flatMap(matchId -> matchApiClient.getMatchById(matchId, "asia"))
                .doOnNext(match ->
                        log.info("Processed match: {}", match.getMetadata().getMatchId())
                );
    }

    /**
     * 예시 5: Riot ID로 모든 정보 한번에 조회
     */
    public Mono<PlayerFullInfo> getPlayerFullInfo(String gameName, String tagLine) {
        return accountApiClient.getByRiotId(gameName, tagLine, "asia")
                .flatMap(account -> {
                    Mono<SummonerDto> summonerMono = summonerApiClient.getByPuuid(
                            account.getPuuid(), "kr"
                    );

                    Mono<List<String>> matchIdsMono = matchApiClient.getCustomMatchIds(
                            account.getPuuid(), "asia", 10
                    );

                    return Mono.zip(Mono.just(account), summonerMono, matchIdsMono)
                            .map(tuple -> new PlayerFullInfo(
                                    tuple.getT1(),
                                    tuple.getT2(),
                                    tuple.getT3()
                            ));
                });
    }

    // DTO for example
    public record PlayerFullInfo(
            AccountDto account,
            SummonerDto summoner,
            List<String> recentCustomMatchIds
    ) {}
}