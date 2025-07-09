package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.ContentTypeEnum;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalizedContent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BjyotishUser bjyotishUser;

    private ContentTypeEnum contentType;

    private String title;

    private String content;

    private LocalDate contentDate;

    @Builder.Default
    private Boolean isSent = false;

    private Instant sentAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

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
