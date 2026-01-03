package com.uhihi.loltracker.domain.player.service;

import com.uhihi.loltracker.domain.player.dto.PlayerRequest;
import com.uhihi.loltracker.domain.player.dto.PlayerResponse;
import com.uhihi.loltracker.domain.player.entity.Player;
import com.uhihi.loltracker.domain.player.exception.PlayerAlreadyExistsException;
import com.uhihi.loltracker.domain.player.exception.PlayerNotFoundException;
import com.uhihi.loltracker.domain.player.repository.PlayerRepository;
import com.uhihi.loltracker.infrastructure.riot.client.AccountApiClient;
import com.uhihi.loltracker.infrastructure.riot.dto.account.AccountDto;
import com.uhihi.loltracker.infrastructure.riot.exception.RiotApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final AccountApiClient accountApiClient;

    // 등록
    @Transactional
    public PlayerResponse registerPlayer(PlayerRequest request) {
        log.info("Registering player: {}", request.getRiotId());

        // 중복 체크
        if (playerRepository.existsByGameNameAndTagLine(request.getGameName(), request.getTagLine())) {
            throw new PlayerAlreadyExistsException(request.getGameName(), request.getTagLine());
        }

        // Riot API로 계정 정보 조회
        AccountDto account;
        try {
            account = accountApiClient
                    .getByRiotId(request.getGameName(), request.getTagLine(), "asia")
                    .block();
        } catch (RiotApiException e) {
            log.error("Failed to fetch account from Riot API: {}", request.getRiotId(), e);
            throw new IllegalArgumentException("플레이어를 찾을 수 없습니다: " + request.getRiotId());
        }

        if (account == null) {
            throw new IllegalArgumentException("플레이어를 찾을 수 없습니다: " + request.getRiotId());
        }

        // PUUID 중복 체크
        if (playerRepository.existsByPuuid(account.getPuuid())) {
            throw PlayerAlreadyExistsException.byPuuid(account.getPuuid());
        }

        // Entity 생성 및 저장
        Player player = Player.builder()
                .puuid(account.getPuuid())
                .gameName(account.getGameName())
                .tagLine(account.getTagLine())
                .lastSyncedAt(LocalDateTime.now())
                .build();

        Player saved = playerRepository.save(player);
        log.info("Player registered successfully: {} (PUUID: {})", saved.getRiotId(), saved.getPuuid());

        return PlayerResponse.from(saved);
    }

    // 전체 플레이어 조회
    public List<PlayerResponse> findAllPlayers() {
        log.debug("Fetching all players");
        return playerRepository.findAll().stream()
                .map(PlayerResponse::from)
                .collect(Collectors.toList());
    }

    // Id 로 조회
    public PlayerResponse findById(Long id) {
        log.debug("Fetching player by id: {}", id);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        return PlayerResponse.from(player);
    }

    // PUUID로 조회
    public PlayerResponse findByPuuid(String puuid) {
        log.debug("Fetching player by PUUID: {}", puuid);
        Player player = playerRepository.findByPuuid(puuid)
                .orElseThrow(() -> PlayerNotFoundException.byPuuid(puuid));
        return PlayerResponse.from(player);
    }

    // 라이엇 아이디로 조회
    public PlayerResponse findByRiotId(String gameName, String tagLine) {
        log.debug("Fetching player by Riot ID: {}#{}", gameName, tagLine);
        Player player = playerRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseThrow(() -> new PlayerNotFoundException(gameName, tagLine));
        return PlayerResponse.from(player);
    }

    // 플레이어 삭제
    @Transactional
    public void deletePlayer(Long id) {
        log.info("Deleting player with id: {}", id);

        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFoundException(id);
        }

        playerRepository.deleteById(id);
        log.info("Player deleted successfully: {}", id);
    }


    // 플레이어 정보 동기화
    @Transactional
    public PlayerResponse syncPlayer(Long id) {
        log.info("Syncing player with id: {}", id);

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        try {
            // Riot API에서 최신 정보 조회
            AccountDto account = accountApiClient
                    .getByPuuid(player.getPuuid(), "asia")
                    .block();

            if (account != null) {
                // 정보 업데이트 (이름 변경 가능성)
                player.updateInfo(account.getGameName(), account.getTagLine());
                player.updateSyncTime();

                Player updated = playerRepository.save(player);
                log.info("Player synced successfully: {}", updated.getRiotId());

                return PlayerResponse.from(updated);
            }
        } catch (RiotApiException e) {
            log.error("Failed to sync player from Riot API: {}", player.getPuuid(), e);
            throw new IllegalStateException("플레이어 동기화 실패", e);
        }

        throw new IllegalStateException("플레이어 동기화 실패");
    }


    // 동기화 필요한 플레이어 조회
    public List<Player> findPlayersNeedingSync(int hours) {
        LocalDateTime threshold = LocalDateTime.now().minusHours(hours);
        return playerRepository.findPlayersNeedingSync(threshold);
    }


    // PUUID 조회
    public List<String> getAllPuuids() {
        return playerRepository.findAllPuuids();
    }


    // 플레이어 검색
    public List<PlayerResponse> searchPlayers(String gameName) {
        log.debug("Searching players with game name containing: {}", gameName);
        return playerRepository.findByGameNameContainingIgnoreCase(gameName).stream()
                .map(PlayerResponse::from)
                .collect(Collectors.toList());
    }


    // 플레이어 통계
    public PlayerStats getPlayerStats() {
        long totalPlayers = playerRepository.countAllPlayers();
        List<Player> recentPlayers = playerRepository.findTop10ByOrderByCreatedAtDesc();

        return PlayerStats.builder()
                .totalPlayers(totalPlayers)
                .recentPlayers(recentPlayers.stream()
                        .map(PlayerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    // Inner class for stats
    @lombok.Data
    @lombok.Builder
    public static class PlayerStats {
        private long totalPlayers;
        private List<PlayerResponse> recentPlayers;
    }
}