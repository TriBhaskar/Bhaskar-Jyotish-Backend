package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.ContentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

// PersonalizedContent Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personalized_content")
public class PersonalizedContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personalized_content_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "personalized_content_sequence_generator", sequenceName = "seq_personalized_content_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentTypeEnum contentType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_date", nullable = false)
    private LocalDate contentDate;

    @Column(name = "is_sent")
    @Builder.Default
    private Boolean isSent = false;

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
    public boolean isSent() {
        return isSent != null && isSent;
    }

    public boolean isPending() {
        return !isSent();
    }

    public String getContentTypeDisplayName() {
        return contentType != null ? contentType.getDisplayName() : "Unknown";
    }

    public String getContentCategory() {
        return contentType != null ? contentType.getCategory() : "General";
    }

    public boolean isHoroscopeContent() {
        return contentType != null && contentType.isHoroscope();
    }

    public boolean isAnalysisContent() {
        return contentType != null && contentType.isAnalysis();
    }

    public boolean isForecastContent() {
        return contentType != null && contentType.isForecast();
    }

    public boolean isRecommendationContent() {
        return contentType != null && contentType.isRecommendation();
    }

    public boolean isHealthContent() {
        return contentType != null && contentType.isHealthRelated();
    }

    public void markAsSent() {
        this.isSent = true;
        this.sentAt = Instant.now();
    }

    public void markAsPending() {
        this.isSent = false;
        this.sentAt = null;
    }

    public boolean isContentForToday() {
        return contentDate != null && contentDate.equals(LocalDate.now());
    }

    public boolean isContentExpired() {
        if (contentDate == null) return false;

        return switch (contentType) {
            case DAILY_HOROSCOPE -> contentDate.isBefore(LocalDate.now());
            case WEEKLY_HOROSCOPE -> contentDate.isBefore(LocalDate.now().minusWeeks(1));
            case MONTHLY_HOROSCOPE -> contentDate.isBefore(LocalDate.now().minusMonths(1));
            default -> false; // Other content types don't expire
        };
    }

    public String getContentSummary() {
        if (content == null || content.trim().isEmpty()) {
            return "No content available";
        }

        String cleanContent = content.trim();
        if (cleanContent.length() <= 100) {
            return cleanContent;
        }

        // Find the last complete sentence within 100 characters
        String truncated = cleanContent.substring(0, 100);
        int lastPeriod = truncated.lastIndexOf('.');
        if (lastPeriod > 50) { // Only use if we have at least 50 chars
            return truncated.substring(0, lastPeriod + 1);
        }

        return truncated + "...";
    }

    public boolean hasValidContent() {
        return title != null && !title.trim().isEmpty() &&
                content != null && !content.trim().isEmpty() &&
                contentDate != null && contentType != null;
    }

    public String getStatusDescription() {
        if (isSent()) {
            return "Sent on " + (sentAt != null ? sentAt.toString() : "unknown date");
        } else if (isContentExpired()) {
            return "Expired";
        } else {
            return "Pending";
        }
    }

    public int getContentWordCount() {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }

    public boolean isContentForDate(LocalDate date) {
        return contentDate != null && contentDate.equals(date);
    }

    public boolean isContentForDateRange(LocalDate startDate, LocalDate endDate) {
        if (contentDate == null || startDate == null || endDate == null) {
            return false;
        }
        return !contentDate.isBefore(startDate) && !contentDate.isAfter(endDate);
    }
}
