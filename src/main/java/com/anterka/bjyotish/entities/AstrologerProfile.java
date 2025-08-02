package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.SpecializationTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// AstrologerProfile Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "astrologer_profiles")
public class AstrologerProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "astrologer_profile_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "astrologer_profile_sequence_generator", sequenceName = "seq_astrologer_profile_id", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false, unique = true)
    private BjyotishUser bjyotishUser;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "years_of_experience")
    @Builder.Default
    private Integer yearsOfExperience = 0;

    @Column(name = "languages_spoken", columnDefinition = "text[]")
    private String[] languagesSpoken;

    @Column(name = "specializations", columnDefinition = "text[]")
//    @Enumerated(EnumType.STRING)
    private String[] specializations;

    @Column(name = "certifications", columnDefinition = "text[]")
    private String[] certifications;

    @Column(name = "consultation_fee_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFeePerHour;

    @Column(name = "minimum_consultation_duration")
    @Builder.Default
    private Integer minimumConsultationDuration = 30; // in minutes

    @Column(name = "is_available_for_consultation")
    @Builder.Default
    private Boolean isAvailableForConsultation = true;

    @Column(name = "rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.valueOf(0.00);

    @Column(name = "total_consultations")
    @Builder.Default
    private Integer totalConsultations = 0;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "verified_astrologer")
    @Builder.Default
    private Boolean verifiedAstrologer = false;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Bidirectional relationships
    @OneToMany(mappedBy = "astrologer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AstrologerAvailability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "astrologer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AstrologerBlockedSlot> blockedSlots = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Helper methods for managing relationships
    public void addAvailability(AstrologerAvailability availability) {
        availabilities.add(availability);
        availability.setAstrologer(this);
    }

    public void removeAvailability(AstrologerAvailability availability) {
        availabilities.remove(availability);
        availability.setAstrologer(null);
    }

    public void addBlockedSlot(AstrologerBlockedSlot blockedSlot) {
        blockedSlots.add(blockedSlot);
        blockedSlot.setAstrologer(this);
    }

    public void removeBlockedSlot(AstrologerBlockedSlot blockedSlot) {
        blockedSlots.remove(blockedSlot);
        blockedSlot.setAstrologer(null);
    }
}
