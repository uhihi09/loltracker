package com.uhihi.loltracker.domain.player.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String puuid; // 라이엇 PUUID
    private String gameName; // 라이엇 ID 게임명
    private String tagLine; // 라이엇 아이디 태그

    private LocalDateTime lastSyncedAt; // 마지막 동기화 시간

}
