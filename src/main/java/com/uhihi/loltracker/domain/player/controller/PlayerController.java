package com.uhihi.loltracker.presentation.controller;

import com.uhihi.loltracker.domain.player.dto.PlayerRequest;
import com.uhihi.loltracker.domain.player.dto.PlayerResponse;
import com.uhihi.loltracker.domain.player.service.PlayerService;
import com.uhihi.loltracker.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Player", description = "플레이어 관리 API")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @Operation(summary = "플레이어 등록", description = "Riot ID로 새로운 플레이어를 등록합니다.")
    public ResponseEntity<ApiResponse<PlayerResponse>> registerPlayer(
            @Valid @RequestBody PlayerRequest request) {
        log.info("POST /api/players - Register player: {}", request.getRiotId());

        PlayerResponse player = playerService.registerPlayer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(player, "플레이어가 성공적으로 등록되었습니다."));
    }

    @GetMapping
    @Operation(summary = "전체 플레이어 조회", description = "등록된 모든 플레이어를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getAllPlayers() {
        log.info("GET /api/players - Get all players");

        List<PlayerResponse> players = playerService.findAllPlayers();

        return ResponseEntity.ok(
                ApiResponse.success(players, players.size() + "명의 플레이어를 찾았습니다.")
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "플레이어 조회", description = "ID로 특정 플레이어를 조회합니다.")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayer(
            @Parameter(description = "플레이어 ID")
            @PathVariable Long id) {
        log.info("GET /api/players/{} - Get player by id", id);

        PlayerResponse player = playerService.findById(id);

        return ResponseEntity.ok(ApiResponse.success(player));
    }

    @GetMapping("/puuid/{puuid}")
    @Operation(summary = "PUUID로 플레이어 조회", description = "PUUID로 플레이어를 조회합니다.")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayerByPuuid(
            @Parameter(description = "플레이어 PUUID")
            @PathVariable String puuid) {
        log.info("GET /api/players/puuid/{} - Get player by PUUID", puuid);

        PlayerResponse player = playerService.findByPuuid(puuid);

        return ResponseEntity.ok(ApiResponse.success(player));
    }

    @GetMapping("/riot-id")
    @Operation(summary = "Riot ID로 플레이어 조회", description = "게임 이름과 태그라인으로 플레이어를 조회합니다.")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayerByRiotId(
            @Parameter(description = "게임 이름")
            @RequestParam String gameName,
            @Parameter(description = "태그라인")
            @RequestParam String tagLine) {
        log.info("GET /api/players/riot-id?gameName={}&tagLine={} - Get player by Riot ID",
                gameName, tagLine);

        PlayerResponse player = playerService.findByRiotId(gameName, tagLine);

        return ResponseEntity.ok(ApiResponse.success(player));
    }

    @GetMapping("/search")
    @Operation(summary = "플레이어 검색", description = "게임 이름으로 플레이어를 검색합니다.")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> searchPlayers(
            @Parameter(description = "검색할 게임 이름")
            @RequestParam String gameName) {
        log.info("GET /api/players/search?gameName={} - Search players", gameName);

        List<PlayerResponse> players = playerService.searchPlayers(gameName);

        return ResponseEntity.ok(
                ApiResponse.success(players, players.size() + "명의 플레이어를 찾았습니다.")
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "플레이어 삭제", description = "ID로 플레이어를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deletePlayer(
            @Parameter(description = "플레이어 ID")
            @PathVariable Long id) {
        log.info("DELETE /api/players/{} - Delete player", id);

        playerService.deletePlayer(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "플레이어가 성공적으로 삭제되었습니다.")
        );
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "플레이어 동기화", description = "Riot API에서 최신 플레이어 정보를 가져와 동기화합니다.")
    public ResponseEntity<ApiResponse<PlayerResponse>> syncPlayer(
            @Parameter(description = "플레이어 ID")
            @PathVariable Long id) {
        log.info("POST /api/players/{}/sync - Sync player", id);

        PlayerResponse player = playerService.syncPlayer(id);

        return ResponseEntity.ok(
                ApiResponse.success(player, "플레이어 정보가 동기화되었습니다.")
        );
    }

    @GetMapping("/stats")
    @Operation(summary = "플레이어 통계", description = "플레이어 통계 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PlayerService.PlayerStats>> getPlayerStats() {
        log.info("GET /api/players/stats - Get player statistics");

        PlayerService.PlayerStats stats = playerService.getPlayerStats();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/puuids")
    @Operation(summary = "모든 PUUID 조회", description = "등록된 모든 플레이어의 PUUID를 조회합니다.")
    public ResponseEntity<ApiResponse<List<String>>> getAllPuuids() {
        log.info("GET /api/players/puuids - Get all PUUIDs");

        List<String> puuids = playerService.getAllPuuids();

        return ResponseEntity.ok(
                ApiResponse.success(puuids, puuids.size() + "개의 PUUID를 찾았습니다.")
        );
    }
}