package com.uhihi.loltracker.infrastructure.riot.client;

import com.uhihi.loltracker.infrastructure.riot.config.RiotApiProperties;
import com.uhihi.loltracker.infrastructure.riot.exception.RateLimitExceededException;
import com.uhihi.loltracker.infrastructure.riot.exception.RiotApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class BaseRiotApiClient {

    protected final WebClient webClient;
    protected final RiotApiProperties properties;

    /**
     * GET 요청 수행
     */
    protected <T> Mono<T> get(String url, Class<T> responseType) {
        log.debug("GET request to: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error: " + body)))
                )
                .bodyToMono(responseType)
                .retryWhen(createRetrySpec(url))
                .doOnSuccess(response -> log.debug("GET success: {}", url))
                .doOnError(error -> log.error("GET failed: {}", url, error));
    }

    /**
     * 에러 핸들링
     */
    protected Mono<? extends Throwable> handleError(ClientResponse response) {
        String endpoint = response.request().getURI().toString();

        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    HttpStatus status = (HttpStatus) response.statusCode();

                    return switch (status) {
                        case NOT_FOUND -> Mono.error(RiotApiException.notFound(endpoint));
                        case UNAUTHORIZED -> Mono.error(RiotApiException.unauthorized(endpoint));
                        case FORBIDDEN -> Mono.error(RiotApiException.forbidden(endpoint));
                        case TOO_MANY_REQUESTS -> {
                            Long retryAfter = response.headers().header("Retry-After")
                                    .stream()
                                    .findFirst()
                                    .map(Long::parseLong)
                                    .orElse(1L);
                            yield Mono.error(new RateLimitExceededException(endpoint, retryAfter));
                        }
                        case SERVICE_UNAVAILABLE -> Mono.error(RiotApiException.serviceUnavailable(endpoint));
                        default -> Mono.error(new RiotApiException(
                                "Riot API error: " + body,
                                status,
                                endpoint
                        ));
                    };
                });
    }

    /**
     * Retry 전략
     */
    protected Retry createRetrySpec(String url) {
        return Retry.backoff(
                        properties.getRetry().getMaxAttempts(),
                        Duration.ofMillis(properties.getRetry().getBackoffMs())
                )
                .filter(throwable -> {
                    // Rate Limit이나 일시적 에러만 재시도
                    return throwable instanceof RateLimitExceededException ||
                            throwable instanceof RiotApiException riotEx &&
                                    riotEx.getStatus() == HttpStatus.SERVICE_UNAVAILABLE;
                })
                .doBeforeRetry(signal ->
                        log.warn("Retrying request to {} (attempt: {})",
                                url, signal.totalRetries() + 1)
                )
                .onRetryExhaustedThrow((spec, signal) -> signal.failure());
    }

    /**
     * 지역별 Base URL 가져오기
     */
    protected String getRegionUrl(String region) {
        String baseUrl = properties.getRegions().get(region.toLowerCase());
        if (baseUrl == null) {
            throw new IllegalArgumentException("Unknown region: " + region);
        }
        return baseUrl;
    }
}