package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
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
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "review_sequence_generator", sequenceName = "seq_review_id", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false, unique = true)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private BjyotishUser client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    private AstrologerProfile astrologer;

    @Column(name = "rating", nullable = false)
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "is_anonymous")
    @Builder.Default
    private Boolean isAnonymous = false;

    @Column(name = "is_approved")
    @Builder.Default
    private Boolean isApproved = false;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
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

