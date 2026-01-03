package com.uhihi.loltracker.domain.player.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "player", indexes = {
        @Index(name = "idx_player_puuid", columnList = "puuid"),
        @Index(name = "idx_player_game_name_tag", columnList = "gameName, tagLine")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String puuid;

    @Column(nullable = false, length = 50)
    private String gameName;

    @Column(nullable = false, length = 10)
    private String tagLine;

    private LocalDateTime lastSyncedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void updateSyncTime() {
        this.lastSyncedAt = LocalDateTime.now();
    }

    public void updateInfo(String gameName, String tagLine) {
        this.gameName = gameName;
        this.tagLine = tagLine;
    }

    public String getRiotId() {
        return gameName + "#" + tagLine;
    }

    public boolean needsSync(int hours) {
        if (lastSyncedAt == null) return true;

        return lastSyncedAt.isBefore(LocalDateTime.now().minusHours(hours));
    }

}
