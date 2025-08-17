package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.ConsultationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

// Consultation Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consultations")
public class Consultation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consultation_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "consultation_sequence_generator", sequenceName = "seq_consultation_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private BjyotishUser client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "astrologer_id", nullable = false)
    private AstrologerProfile astrologer;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "scheduled_start_time", nullable = false)
    private LocalTime scheduledStartTime;

    @Column(name = "scheduled_end_time", nullable = false)
    private LocalTime scheduledEndTime;

    @Column(name = "actual_start_time")
    private Instant actualStartTime;

    @Column(name = "actual_end_time")
    private Instant actualEndTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private ConsultationStatusEnum status = ConsultationStatusEnum.REQUESTED;

    @Column(name = "consultation_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(name = "meeting_url", length = 500)
    private String meetingUrl;

    @Column(name = "meeting_id", length = 100)
    private String meetingId;

//    @Column(name = "recording_url", length = 500)
//    private String recordingUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "client_questions", columnDefinition = "TEXT")
    private String clientQuestions;

    @Column(name = "astrologer_notes", columnDefinition = "TEXT")
    private String astrologerNotes;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Bidirectional relationship with reschedules
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationReschedule> reschedules = new ArrayList<>();

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
    public void addReschedule(ConsultationReschedule reschedule) {
        reschedules.add(reschedule);
        reschedule.setConsultation(this);
    }

    public void removeReschedule(ConsultationReschedule reschedule) {
        reschedules.remove(reschedule);
        reschedule.setConsultation(null);
    }

    // Utility methods
    public LocalDateTime getScheduledDateTime() {
        return LocalDateTime.of(scheduledDate, scheduledStartTime);
    }

    public Duration getScheduledDuration() {
        return Duration.between(scheduledStartTime, scheduledEndTime);
    }

    public Duration getActualDuration() {
        if (actualStartTime != null && actualEndTime != null) {
            return Duration.between(actualStartTime, actualEndTime);
        }
        return null;
    }

    public boolean isCompleted() {
        return status == ConsultationStatusEnum.COMPLETED;
    }

    public boolean isActive() {
        return status == ConsultationStatusEnum.IN_PROGRESS;
    }

    public boolean canBeRescheduled() {
        return status == ConsultationStatusEnum.REQUESTED ||
                status == ConsultationStatusEnum.CONFIRMED;
    }
}
