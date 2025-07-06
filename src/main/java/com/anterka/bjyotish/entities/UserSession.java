package com.anterka.bjyotish.entities;

// ==============================================
// USER SESSION ENTITY
// ==============================================

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_sessions")
public class UserSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_session_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "user_session_sequence_generator", sequenceName = "seq_user_session_id", allocationSize = 1)
    private Long id;

    @Column(name = "bjyotish_user_id", nullable = false)
    private Long bjyotishUserId;

    @Column(name = "session_token", nullable = false, unique = true)
    private String sessionToken;

    @Column(name = "refresh_token", unique = true)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "ip_address")
    @JdbcTypeCode(SqlTypes.INET)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", insertable = false, updatable = false)
    private BjyotishUser bjyotishUser;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean isActive() {
        return isActive != null && isActive;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public boolean isValid() {
        return isActive() && !isExpired();
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void expire() {
        this.expiresAt = Instant.now();
    }

    public void invalidate() {
        deactivate();
        expire();
    }

    public long getMinutesUntilExpiry() {
        if (expiresAt == null) return 0;
        return Math.max(0, java.time.Duration.between(Instant.now(), expiresAt).toMinutes());
    }

    public long getHoursUntilExpiry() {
        if (expiresAt == null) return 0;
        return Math.max(0, java.time.Duration.between(Instant.now(), expiresAt).toHours());
    }

    public long getDaysUntilExpiry() {
        if (expiresAt == null) return 0;
        return Math.max(0, java.time.Duration.between(Instant.now(), expiresAt).toDays());
    }

    public boolean isExpiringSoon() {
        return getHoursUntilExpiry() <= 24; // Expires within 24 hours
    }

    public boolean isExpiringSoon(long hours) {
        return getHoursUntilExpiry() <= hours;
    }

    public long getSessionDurationMinutes() {
        if (createdAt == null) return 0;
        Instant endTime = isExpired() ? expiresAt : Instant.now();
        return java.time.Duration.between(createdAt, endTime).toMinutes();
    }

    public boolean isLongRunningSession() {
        return getSessionDurationMinutes() > 480; // More than 8 hours
    }

    public String getUserName() {
        return bjyotishUser != null ? bjyotishUser.getFirstName() + " " + bjyotishUser.getLastName() : "Unknown User";
    }

    public String getUserEmail() {
        return bjyotishUser != null ? bjyotishUser.getEmail() : "Unknown Email";
    }

    public boolean hasRefreshToken() {
        return refreshToken != null && !refreshToken.trim().isEmpty();
    }

    public boolean hasUserAgent() {
        return userAgent != null && !userAgent.trim().isEmpty();
    }

    public boolean isWebBrowser() {
        return hasUserAgent() && (
                userAgent.contains("Mozilla") ||
                        userAgent.contains("Chrome") ||
                        userAgent.contains("Safari") ||
                        userAgent.contains("Firefox")
        );
    }

    public boolean isMobileDevice() {
        return hasUserAgent() && (
                userAgent.contains("Mobile") ||
                        userAgent.contains("Android") ||
                        userAgent.contains("iPhone") ||
                        userAgent.contains("iPad")
        );
    }

    public String getDeviceType() {
        if (!hasUserAgent()) return "Unknown";
        if (isMobileDevice()) return "Mobile";
        if (isWebBrowser()) return "Web Browser";
        return "Other";
    }

    public String getBrowserName() {
        if (!hasUserAgent()) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("firefox")) return "Firefox";
        if (ua.contains("chrome")) return "Chrome";
        if (ua.contains("safari") && !ua.contains("chrome")) return "Safari";
        if (ua.contains("edge")) return "Edge";
        if (ua.contains("opera")) return "Opera";
        return "Other";
    }

    public String getOperatingSystem() {
        if (!hasUserAgent()) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("windows")) return "Windows";
        if (ua.contains("mac os")) return "macOS";
        if (ua.contains("linux")) return "Linux";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone") || ua.contains("ipad")) return "iOS";
        return "Other";
    }

    public String getSessionStatus() {
        if (!isActive()) return "Inactive";
        if (isExpired()) return "Expired";
        if (isExpiringSoon()) return "Expiring Soon";
        return "Active";
    }

    public String getSessionSummary() {
        return String.format("%s - %s (%s) - %s",
                getUserName(),
                getDeviceType(),
                getBrowserName(),
                getSessionStatus());
    }

    public boolean isRecentSession() {
        return createdAt != null && createdAt.isAfter(Instant.now().minusSeconds(3600)); // Last hour
    }

    public boolean isTodaySession() {
        if (createdAt == null) return false;
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate sessionDate = createdAt.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        return sessionDate.equals(today);
    }

    public void extendSession(long additionalMinutes) {
        this.expiresAt = expiresAt.plusSeconds(additionalMinutes * 60);
    }

    public void refreshSession(long sessionDurationMinutes) {
        this.expiresAt = Instant.now().plusSeconds(sessionDurationMinutes * 60);
        this.updatedAt = Instant.now();
    }

    public boolean canRefresh() {
        return hasRefreshToken() && !isExpired();
    }

    public String getExpiryDescription() {
        if (isExpired()) return "Expired";

        long hours = getHoursUntilExpiry();
        long minutes = getMinutesUntilExpiry();

        if (hours > 24) return getDaysUntilExpiry() + " days";
        if (hours > 0) return hours + " hours";
        return minutes + " minutes";
    }

    public boolean isSuspiciousSession() {
        // Simple heuristics for suspicious sessions
        return isLongRunningSession() ||
                (ipAddress != null && !isValidIpAddress(ipAddress)) ||
                (!hasUserAgent());
    }

    private boolean isValidIpAddress(String ip) {
        // Basic IP validation - you might want more sophisticated validation
        return ip != null && ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }
}
