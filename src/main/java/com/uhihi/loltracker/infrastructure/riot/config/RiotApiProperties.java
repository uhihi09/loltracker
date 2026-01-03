package com.uhihi.loltracker.infrastructure.riot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "riot.api")
public class RiotApiProperties {

    private String key;
    private Map<String, String> regions;
    private RateLimit rateLimit;
    private Timeout timeout;
    private Retry retry;

    @Getter
    @Setter
    public static class RateLimit {
        private int perSecond;
        private int perTwoMinutes;
    }

    @Getter
    @Setter
    public static class Timeout {
        private int connect;
        private int read;
    }

    @Getter
    @Setter
    public static class Retry {
        private int maxAttempts;
        private long backoffMs;
    }
}