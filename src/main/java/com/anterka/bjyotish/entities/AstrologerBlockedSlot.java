package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

// AstrologerBlockedSlot Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "astrologer_blocked_slots")
public class AstrologerBlockedSlot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "astrologer_blocked_slot_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "astrologer_blocked_slot_sequence_generator", sequenceName = "seq_astrologer_blocked_slot_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    private AstrologerProfile astrologer;

    @Column(name = "blocked_date", nullable = false)
    private LocalDate blockedDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
