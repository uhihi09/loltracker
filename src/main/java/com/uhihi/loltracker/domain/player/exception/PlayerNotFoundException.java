package com.uhihi.loltracker.domain.player.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(Long id) {
        super("Player not found with id: " + id);
    }

    public PlayerNotFoundException(String gameName, String tagLine) {
        super("Player not found: " + gameName + "#" + tagLine);
    }

    public static PlayerNotFoundException byPuuid(String puuid) {
        return new PlayerNotFoundException("Player not found with PUUID: " + puuid);
    }
}