package com.anterka.bjyotish.scheduler;

import com.anterka.bjyotish.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "jwt.security.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public class TokenCleanupScheduler {

    private final RefreshTokenService refreshTokenService;

    /**
     * Runs daily at 2 AM to clean up expired refresh tokens
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        try {
            refreshTokenService.cleanupExpiredTokens();
            log.info("Successfully completed cleanup of expired refresh tokens");
        } catch (Exception e) {
            log.error("Error occurred during token cleanup", e);
        }
    }
}
