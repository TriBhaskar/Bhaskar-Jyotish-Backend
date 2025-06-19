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
import java.time.LocalTime;

// AstrologerAvailability Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "astrologer_availability")
public class AstrologerAvailability implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "astrologer_availability_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "astrologer_availability_sequence_generator", sequenceName = "seq_astrologer_availability_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    private AstrologerProfile astrologer;

    @Column(name = "day_of_week", nullable = false)
    @Min(0)
    @Max(6)
    private Integer dayOfWeek; // 0 = Sunday, 1 = Monday, etc.

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
