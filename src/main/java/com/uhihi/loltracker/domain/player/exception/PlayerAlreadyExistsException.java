package com.uhihi.loltracker.domain.player.exception;

public class PlayerAlreadyExistsException extends RuntimeException {

    public PlayerAlreadyExistsException(String message) {
        super(message);
    }

    public PlayerAlreadyExistsException(String gameName, String tagLine) {
        super("Player already exists: " + gameName + "#" + tagLine);
    }

    public static PlayerAlreadyExistsException byPuuid(String puuid) {
        return new PlayerAlreadyExistsException("Player already exists with PUUID: " + puuid);
    }
}