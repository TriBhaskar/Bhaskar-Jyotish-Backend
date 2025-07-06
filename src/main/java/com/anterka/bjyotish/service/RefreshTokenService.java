package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.UserSessionRepository;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.entities.UserSession;
import com.anterka.bjyotish.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final UserSessionRepository userSessionRepository;
    private final JwtUtils jwtUtils;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${jwt.security.refresh-token.expiration.time.in-days:30}")
    private int refreshTokenExpirationInDays;

    @Value("${jwt.security.refresh-token.length:64}")
    private int refreshTokenLength;

    @Value("${jwt.security.max-active-sessions:5}")
    private int maxActiveSessions;

    /**
     * Creates a new user session with access token and refresh token
     */
    @Transactional
    public UserSession createSession(BjyotishUser user, String accessToken, HttpServletRequest request) {
        // Check if user has too many active sessions
        long activeSessionCount = userSessionRepository.countActiveSessionsForUser(user.getId());
        if (activeSessionCount >= maxActiveSessions) {
            // Deactivate oldest session or all sessions based on business logic
            log.info("User {} has reached max active sessions limit. Deactivating oldest session.", user.getEmail());
            deactivateOldestSession(user.getId());
        }

        String refreshToken = generateRefreshToken();
        Instant refreshTokenExpiresAt = Instant.now().plusSeconds(refreshTokenExpirationInDays * 24 * 60 * 60L);

        UserSession session = UserSession.builder()
                .bjyotishUserId(user.getId())
                .sessionToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(refreshTokenExpiresAt)
                .isActive(true)
                .ipAddress(getClientIpAddress(request))
                .userAgent(request.getHeader("User-Agent"))
                .build();

        UserSession savedSession = userSessionRepository.save(session);
        log.info("Created new session for user: {}", user.getEmail());
        return savedSession;
    }

    /**
     * Generates a new access token using the refresh token
     */
    @Transactional
    public Optional<TokenRefreshResult> refreshAccessToken(String refreshToken, HttpServletRequest request) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshTokenAndIsActiveTrue(refreshToken);

        if (sessionOpt.isEmpty()) {
            log.warn("Invalid or expired refresh token provided");
            return Optional.empty();
        }

        UserSession session = sessionOpt.get();

        // Check if refresh token is expired
        if (session.getExpiresAt().isBefore(Instant.now())) {
            log.warn("Refresh token has expired for user session: {}", session.getId());
            session.setIsActive(false);
            userSessionRepository.save(session);
            return Optional.empty();
        }

        // Generate new access token
        BjyotishUser user = session.getBjyotishUser();
        String newAccessToken = jwtUtils.generateJwtToken(user.getEmail());

        // Update session with new access token
        session.setSessionToken(newAccessToken);
        session.setIpAddress(getClientIpAddress(request));
        session.setUserAgent(request.getHeader("User-Agent"));

        userSessionRepository.save(session);

        log.info("Successfully refreshed access token for user: {}", user.getEmail());

        return Optional.of(TokenRefreshResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .expiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + jwtUtils.getJwtExpirationTimeInMillis()))
                .user(user)
                .build());
    }

    /**
     * Validates if a refresh token is valid and active
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            return false;
        }

        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshTokenAndIsActiveTrue(refreshToken);

        if (sessionOpt.isEmpty()) {
            return false;
        }

        UserSession session = sessionOpt.get();
        return session.getExpiresAt().isAfter(Instant.now());
    }

    /**
     * Revokes a refresh token (logout)
     */
    @Transactional
    public boolean revokeRefreshToken(String refreshToken) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshTokenAndIsActiveTrue(refreshToken);

        if (sessionOpt.isEmpty()) {
            return false;
        }

        UserSession session = sessionOpt.get();
        session.setIsActive(false);
        userSessionRepository.save(session);

        log.info("Revoked refresh token for session: {}", session.getId());
        return true;
    }

    /**
     * Revokes all refresh tokens for a user (logout from all devices)
     */
    @Transactional
    public void revokeAllRefreshTokensForUser(Long userId) {
        userSessionRepository.deactivateAllSessionsForUser(userId);
        log.info("Revoked all refresh tokens for user: {}", userId);
    }

    /**
     * Generates a cryptographically secure random refresh token
     */
    private String generateRefreshToken() {
        byte[] tokenBytes = new byte[refreshTokenLength];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Cleans up expired refresh tokens
     */
    @Transactional
    public void cleanupExpiredTokens() {
        userSessionRepository.deleteExpiredSessions(Instant.now());
        log.info("Cleaned up expired refresh tokens");
    }

    /**
     * Deactivates the oldest session for a user
     */
    private void deactivateOldestSession(Long userId) {
        userSessionRepository.findByBjyotishUserIdAndIsActiveTrue(userId)
                .stream()
                .min((s1, s2) -> s1.getCreatedAt().compareTo(s2.getCreatedAt()))
                .ifPresent(oldestSession -> {
                    oldestSession.setIsActive(false);
                    userSessionRepository.save(oldestSession);
                    log.info("Deactivated oldest session: {}", oldestSession.getId());
                });
    }

    /**
     * Extracts client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * Result class for token refresh operation
     */
    @lombok.Data
    @lombok.Builder
    public static class TokenRefreshResult {
        private String accessToken;
        private String refreshToken;
        private Instant expiresAt;
        private BjyotishUser user;
    }
}
