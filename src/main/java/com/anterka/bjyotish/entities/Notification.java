package com.anterka.bjyotish.entities;

// ==============================================
// NOTIFICATION ENTITY
// ==============================================

import com.anterka.bjyotish.constants.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

import static com.anterka.bjyotish.constants.NotificationTypeEnum.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "notification_sequence_generator", sequenceName = "seq_notification_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationTypeEnum type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "is_sent")
    @Builder.Default
    private Boolean isSent = false;

    @Column(name = "scheduled_at")
    private Instant scheduledAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public boolean isRead() {
        return isRead != null && isRead;
    }

    public boolean isSent() {
        return isSent != null && isSent;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsUnread() {
        this.isRead = false;
    }

    public void markAsSent() {
        this.isSent = true;
        this.sentAt = Instant.now();
    }

    public boolean isScheduled() {
        return scheduledAt != null && scheduledAt.isAfter(Instant.now());
    }

    public boolean isPending() {
        return !isSent() && (scheduledAt == null || scheduledAt.isBefore(Instant.now()));
    }

    public boolean isOverdue() {
        return !isSent() && scheduledAt != null && scheduledAt.isBefore(Instant.now());
    }

    public boolean isConsultationRelated() {
        return type == NotificationTypeEnum.CONSULTATION_REMINDER ||
                type == NotificationTypeEnum.CONSULTATION_CONFIRMED ||
                type == NotificationTypeEnum.CONSULTATION_CANCELLED;
    }

    public boolean isSystemRelated() {
        return type == SYSTEM_NOTIFICATION ||
                type == SUBSCRIPTION_EXPIRY;
    }

    public boolean isAstrologyRelated() {
        return type == NotificationTypeEnum.DAILY_HOROSCOPE ||
                type == NotificationTypeEnum.TRANSIT_ALERT;
    }

    public String getRecipientName() {
        return bjyotishUser != null ? bjyotishUser.getFirstName() : "Unknown User";
    }

    public String getRecipientEmail() {
        return bjyotishUser != null ? bjyotishUser.getEmail() : "Unknown Email";
    }

    public String getTypeDescription() {
        if (type == null) return "Unknown";
        return switch (type) {
            case CONSULTATION_REMINDER -> "Consultation Reminder";
            case CONSULTATION_CONFIRMED -> "Consultation Confirmed";
            case CONSULTATION_CANCELLED -> "Consultation Cancelled";
            case SUBSCRIPTION_EXPIRY -> "Subscription Expiry";
            case DAILY_HOROSCOPE -> "Daily Horoscope";
            case TRANSIT_ALERT -> "Transit Alert";
            case SYSTEM_NOTIFICATION -> "System Notification";
        };
    }

    public String getStatusDescription() {
        if (isSent()) return "Sent";
        if (isScheduled()) return "Scheduled";
        if (isPending()) return "Pending";
        if (isOverdue()) return "Overdue";
        return "Unknown";
    }

    public long getMinutesUntilScheduled() {
        if (scheduledAt == null) return 0;
        return java.time.Duration.between(Instant.now(), scheduledAt).toMinutes();
    }

    public long getMinutesSinceSent() {
        if (sentAt == null) return 0;
        return java.time.Duration.between(sentAt, Instant.now()).toMinutes();
    }

    public boolean isRecentlyCreated() {
        return createdAt != null && createdAt.isAfter(Instant.now().minusSeconds(3600)); // Last hour
    }

    public boolean isRecentlySent() {
        return sentAt != null && sentAt.isAfter(Instant.now().minusSeconds(3600)); // Last hour
    }

    public String getMessagePreview(int maxLength) {
        if (message == null || message.trim().isEmpty()) {
            return "No message content";
        }
        return message.length() > maxLength ? message.substring(0, maxLength) + "..." : message;
    }

    public String getNotificationSummary() {
        return String.format("%s - %s (%s)", getTypeDescription(), title, getStatusDescription());
    }
}

