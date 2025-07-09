package com.anterka.bjyotish.entities;

import jakarta.persistence.Entity;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserSessionRecord(
        Long id,
        Long bjyotishUserId,
        String sessionToken,
        String refreshToken,
        Instant expiresAt,
        Boolean isActive,
        String ipAddress,
        String userAgent,
        Instant createdAt,
        Instant updatedAt
) {}
