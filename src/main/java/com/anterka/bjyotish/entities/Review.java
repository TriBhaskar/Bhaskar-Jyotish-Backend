package com.anterka.bjyotish.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

// Review Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Consultation consultation;

    private BjyotishUser client;

    private AstrologerProfile astrologer;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String reviewText;

    @Builder.Default
    private Boolean isAnonymous = false;

    @Builder.Default
    private Boolean isApproved = false;

    @Builder.Default
    private Instant createdAt = Instant.now();

    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public boolean isPositiveReview() {
        return rating >= 4;
    }

    public boolean isNegativeReview() {
        return rating <= 2;
    }

    public boolean hasReviewText() {
        return reviewText != null && !reviewText.trim().isEmpty();
    }

    public String getDisplayName() {
        return isAnonymous ? "Anonymous User" : client.getFirstName() + " " + client.getLastName();
    }
}

