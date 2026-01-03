package com.uhihi.loltracker.infrastructure.riot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RiotApiException extends RuntimeException {

    private final HttpStatus status;
    private final String endpoint;

    public RiotApiException(String message, HttpStatus status, String endpoint) {
        super(message);
        this.status = status;
        this.endpoint = endpoint;
    }

    public RiotApiException(String message, HttpStatus status, String endpoint, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.endpoint = endpoint;
    }

    public static RiotApiException notFound(String endpoint) {
        return new RiotApiException("Resource not found", HttpStatus.NOT_FOUND, endpoint);
    }

    public static RiotApiException unauthorized(String endpoint) {
        return new RiotApiException("Invalid API key", HttpStatus.UNAUTHORIZED, endpoint);
    }

    public static RiotApiException forbidden(String endpoint) {
        return new RiotApiException("API key expired or blocked", HttpStatus.FORBIDDEN, endpoint);
    }

    public static RiotApiException serviceUnavailable(String endpoint) {
        return new RiotApiException("Riot API service unavailable", HttpStatus.SERVICE_UNAVAILABLE, endpoint);
    }
}