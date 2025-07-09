package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.UserSessionRecord;

import java.time.Duration;
import java.time.Instant;

public class UserSessionUtils {
    public static boolean isActive(UserSessionRecord session) {
        return session.isActive() != null && session.isActive();
    }

    public static boolean isExpired(UserSessionRecord session) {
        return session.expiresAt() != null && session.expiresAt().isBefore(Instant.now());
    }

    public static boolean isValid(UserSessionRecord session) {
        return isActive(session) && !isExpired(session);
    }

    public static UserSessionRecord activate(UserSessionRecord session) {
        return new UserSessionRecord(
                session.id(),
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                session.expiresAt(),
                true,
                session.ipAddress(),
                session.userAgent(),
                session.createdAt(),
                session.updatedAt()
        );
    }

    public static UserSessionRecord deactivate(UserSessionRecord session) {
        return new UserSessionRecord(
                session.id(),
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                session.expiresAt(),
                false,
                session.ipAddress(),
                session.userAgent(),
                session.createdAt(),
                session.updatedAt()
        );
    }

    public static UserSessionRecord expire(UserSessionRecord session) {
        return new UserSessionRecord(
                session.id(),
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                Instant.now(),
                session.isActive(),
                session.ipAddress(),
                session.userAgent(),
                session.createdAt(),
                session.updatedAt()
        );
    }

    public static UserSessionRecord extendSession(UserSessionRecord session, long additionalMinutes) {
        return new UserSessionRecord(
                session.id(),
                session.bjyotishUserId(),
                session.sessionToken(),
                session.refreshToken(),
                session.expiresAt().plusSeconds(additionalMinutes * 60),
                session.isActive(),
                session.ipAddress(),
                session.userAgent(),
                session.createdAt(),
                Instant.now()
        );
    }

    public static long getMinutesUntilExpiry(UserSessionRecord session) {
        if (session.expiresAt() == null) return 0;
        return Math.max(0, Duration.between(Instant.now(), session.expiresAt()).toMinutes());
    }

    public static long getHoursUntilExpiry(UserSessionRecord session) {
        if (session.expiresAt() == null) return 0;
        return Math.max(0, Duration.between(Instant.now(), session.expiresAt()).toHours());
    }

    public static boolean isExpiringSoon(UserSessionRecord session) {
        return getHoursUntilExpiry(session) <= 24; // Expires within 24 hours
    }

    public static boolean isExpiringSoon(UserSessionRecord session, long hours) {
        return getHoursUntilExpiry(session) <= hours;
    }

    public static boolean hasRefreshToken(UserSessionRecord session) {
        return session.refreshToken() != null && !session.refreshToken().trim().isEmpty();
    }
}
