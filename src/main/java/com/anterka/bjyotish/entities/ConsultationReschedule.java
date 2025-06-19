package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

// ConsultationReschedule Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consultation_reschedules")
public class ConsultationReschedule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultation_reschedule_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "consultation_reschedule_sequence_generator", sequenceName = "seq_consultation_reschedule_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Column(name = "old_scheduled_date", nullable = false)
    private LocalDate oldScheduledDate;

    @Column(name = "old_start_time", nullable = false)
    private LocalTime oldStartTime;

    @Column(name = "old_end_time", nullable = false)
    private LocalTime oldEndTime;

    @Column(name = "new_scheduled_date", nullable = false)
    private LocalDate newScheduledDate;

    @Column(name = "new_start_time", nullable = false)
    private LocalTime newStartTime;

    @Column(name = "new_end_time", nullable = false)
    private LocalTime newEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rescheduled_by", nullable = false)
    private BjyotishUser rescheduledBy;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public LocalDateTime getOldScheduledDateTime() {
        return LocalDateTime.of(oldScheduledDate, oldStartTime);
    }

    public LocalDateTime getNewScheduledDateTime() {
        return LocalDateTime.of(newScheduledDate, newStartTime);
    }

    public Duration getOldScheduledDuration() {
        return Duration.between(oldStartTime, oldEndTime);
    }

    public Duration getNewScheduledDuration() {
        return Duration.between(newStartTime, newEndTime);
    }

    public boolean isDateChanged() {
        return !oldScheduledDate.equals(newScheduledDate);
    }

    public boolean isTimeChanged() {
        return !oldStartTime.equals(newStartTime) || !oldEndTime.equals(newEndTime);
    }
}
