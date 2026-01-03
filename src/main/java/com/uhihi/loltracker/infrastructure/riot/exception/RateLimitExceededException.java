package com.uhihi.loltracker.infrastructure.riot.exception;

import lombok.Getter;

@Getter
public class RateLimitExceededException extends RiotApiException {

  private final Long retryAfter;

  public RateLimitExceededException(String endpoint, Long retryAfter) {
    super("Rate limit exceeded. Retry after: " + retryAfter + " seconds",
            org.springframework.http.HttpStatus.TOO_MANY_REQUESTS,
            endpoint);
    this.retryAfter = retryAfter;
  }
}