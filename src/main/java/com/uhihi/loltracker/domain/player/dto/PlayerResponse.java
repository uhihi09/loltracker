package com.uhihi.loltracker.domain.player.dto;

import com.uhihi.loltracker.domain.player.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponse {

    private Long id;
    private String puuid;
    private String gameName;
    private String tagLine;
    private String riotId;
    private LocalDateTime lastSyncedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> Response 변환
    public static PlayerResponse from(Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .puuid(player.getPuuid())
                .gameName(player.getGameName())
                .tagLine(player.getTagLine())
                .riotId(player.getRiotId())
                .lastSyncedAt(player.getLastSyncedAt())
                .createdAt(player.getCreatedAt())
                .updatedAt(player.getUpdatedAt())
                .build();
    }
}