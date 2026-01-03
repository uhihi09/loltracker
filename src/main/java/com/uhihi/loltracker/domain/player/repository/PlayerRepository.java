package com.uhihi.loltracker.domain.player.repository;

import com.uhihi.loltracker.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByPuuid(String puuid);

    Optional<Player> findByGameNameAndTagLine(String gameName, String tagLine);

    boolean existsByPuuid(String puuid);

    boolean existsByGameNameAndTagLine(String gameName, String tagLine);

    @Query("SELECT p.puuid FROM Player p")
    List<String> findAllPuuids();

    @Query("SELECT p FROM Player p WHERE p.lastSyncedAt IS NULL OR p.lastSyncedAt < :threshold")
    List<Player> findPlayersNeedingSync(@Param("threshold") LocalDateTime threshold);

    List<Player> findByGameNameContainingIgnoreCase(String gameName);

    List<Player> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(p) FROM Player p")
    long countAllPlayers();
}