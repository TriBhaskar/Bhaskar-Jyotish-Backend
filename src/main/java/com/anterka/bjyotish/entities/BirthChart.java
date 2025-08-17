package com.anterka.bjyotish.entities;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "birth_charts")
public class BirthChart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "birth_chart_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "birth_chart_sequence_generator", sequenceName = "seq_birth_chart_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bjyotish_user_id", nullable = false)
    private BjyotishUser bjyotishUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "birth_detail_id", nullable = false)
    private BirthDetails birthDetail;

    @Column(name = "chart_data", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode chartData;

    @Column(name = "rashi", length = 50)
    private String rashi; // Moon sign

    @Column(name = "lagna", length = 50)
    private String lagna; // Ascendant

    @Column(name = "nakshatra", length = 50)
    private String nakshatra; // Birth star

//    @Column(name = "chart_image_url")
//    private String chartImageUrl;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "birthChart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanetaryPosition> planetaryPositions;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean hasRashi() {
        return rashi != null && !rashi.trim().isEmpty();
    }

    public boolean hasLagna() {
        return lagna != null && !lagna.trim().isEmpty();
    }

    public boolean hasNakshatra() {
        return nakshatra != null && !nakshatra.trim().isEmpty();
    }

//    public boolean hasChartImage() {
//        return chartImageUrl != null && !chartImageUrl.trim().isEmpty();
//    }

    public String getChartSummary() {
        StringBuilder summary = new StringBuilder();
        if (hasRashi()) summary.append("Rashi: ").append(rashi);
        if (hasLagna()) {
            if (summary.length() > 0) summary.append(", ");
            summary.append("Lagna: ").append(lagna);
        }
        if (hasNakshatra()) {
            if (summary.length() > 0) summary.append(", ");
            summary.append("Nakshatra: ").append(nakshatra);
        }
        return summary.toString();
    }

    public int getPlanetaryPositionsCount() {
        return planetaryPositions != null ? planetaryPositions.size() : 0;
    }
}
