package com.anterka.bjyotish.entities;

// ==============================================
// AUDIT LOG ENTITY
// ==============================================

import com.anterka.bjyotish.constants.enums.AuditActionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit_logs")
public class AuditLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "audit_log_sequence_generator", sequenceName = "seq_audit_log_id", allocationSize = 1)
    private Long id;

    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private AuditActionEnum action;

    @Column(name = "old_values", columnDefinition = "JSONB")
    private String oldValues; // JSON string

    @Column(name = "new_values", columnDefinition = "JSONB")
    private String newValues; // JSON string

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private BjyotishUser changedBy;

    @Column(name = "changed_at")
    @Builder.Default
    private Instant changedAt = Instant.now();

    @Column(name = "ip_address")
    private String ipAddress; // INET stored as string

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @PrePersist
    protected void onCreate() {
        changedAt = Instant.now();
    }

    // Utility methods
    public boolean isInsertAction() {
        return action == AuditActionEnum.INSERT;
    }

    public boolean isUpdateAction() {
        return action == AuditActionEnum.UPDATE;
    }

    public boolean isDeleteAction() {
        return action == AuditActionEnum.DELETE;
    }

    public boolean hasOldValues() {
        return oldValues != null && !oldValues.trim().isEmpty();
    }

    public boolean hasNewValues() {
        return newValues != null && !newValues.trim().isEmpty();
    }

    public boolean hasChangedBy() {
        return changedBy != null;
    }

    public String getChangedByName() {
        return changedBy != null ? changedBy.getFirstName() + " " + changedBy.getLastName() : "System";
    }

    public String getChangedByEmail() {
        return changedBy != null ? changedBy.getEmail() : "system@bjyotish.com";
    }

    public String getActionDescription() {
        return switch (action) {
            case INSERT -> "Created";
            case UPDATE -> "Updated";
            case DELETE -> "Deleted";
        };
    }

    public String getRecordIdentifier() {
        return tableName + "#" + recordId;
    }

    public boolean isRecentChange() {
        return changedAt != null && changedAt.isAfter(Instant.now().minusSeconds(3600)); // Last hour
    }

    public boolean isTodayChange() {
        if (changedAt == null) return false;
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate changeDate = changedAt.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        return changeDate.equals(today);
    }

    public long getHoursSinceChange() {
        if (changedAt == null) return 0;
        return java.time.Duration.between(changedAt, Instant.now()).toHours();
    }

    public long getDaysSinceChange() {
        if (changedAt == null) return 0;
        return java.time.Duration.between(changedAt, Instant.now()).toDays();
    }

    public String getAuditSummary() {
        return String.format("%s %s in %s by %s",
                getActionDescription(),
                getRecordIdentifier(),
                tableName,
                getChangedByName());
    }

    public boolean isUserAction() {
        return changedBy != null;
    }

    public boolean isSystemAction() {
        return changedBy == null;
    }

    public boolean isSensitiveTable() {
        return tableName != null && (
                tableName.toLowerCase().contains("user") ||
                        tableName.toLowerCase().contains("payment") ||
                        tableName.toLowerCase().contains("order") ||
                        tableName.toLowerCase().contains("consultation")
        );
    }

    public String getChangeContext() {
        StringBuilder context = new StringBuilder();
        context.append(getActionDescription()).append(" ").append(tableName);
        if (hasChangedBy()) {
            context.append(" by ").append(getChangedByName());
        }
        if (ipAddress != null) {
            context.append(" from ").append(ipAddress);
        }
        return context.toString();
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
}
