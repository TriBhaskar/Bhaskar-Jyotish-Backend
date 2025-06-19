package com.anterka.bjyotish.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "birth_details")
public class BirthDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "birth_detail_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "birth_detail_sequence_generator", sequenceName = "seq_birth_detail_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "birth_time", nullable = false)
    private LocalTime birthTime;

    @Column(name = "birth_place", nullable = false, length = 255)
    private String birthPlace;

    @Column(name = "birth_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal birthLatitude;

    @Column(name = "birth_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal birthLongitude;

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;

    @Column(name = "is_birth_time_accurate")
    @Builder.Default
    private Boolean isBirthTimeAccurate = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
