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

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "planetary_positions")
public class PlanetaryPosition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planetary_position_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "planetary_position_sequence_generator", sequenceName = "seq_planetary_position_id", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "birth_chart_id", nullable = false)
    private BirthChart birthChart;

    @Column(name = "planet", length = 50, nullable = false)
    private String planet;

    @Column(name = "sign", length = 50, nullable = false)
    private String sign;

    @Column(name = "degree", precision = 5, scale = 2, nullable = false)
    private BigDecimal degree;

    @Column(name = "house", nullable = false)
    private Integer house;

    @Column(name = "retrograde")
    @Builder.Default
    private Boolean retrograde = false;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Utility methods
    public boolean isRetrograde() {
        return retrograde != null && retrograde;
    }

    public String getPositionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(planet).append(" in ").append(sign);
        summary.append(" (House ").append(house).append(")");
        summary.append(" at ").append(degree).append("°");
        if (isRetrograde()) {
            summary.append(" (R)");
        }
        return summary.toString();
    }

    public boolean isInSameSign(PlanetaryPosition other) {
        return other != null && sign != null && sign.equals(other.getSign());
    }

    public boolean isInSameHouse(PlanetaryPosition other) {
        return other != null && house != null && house.equals(other.getHouse());
    }

    public String getDegreeMinuteSecond() {
        if (degree == null) return "0°00'00\"";

        double totalDegrees = degree.doubleValue();
        int degrees = (int) totalDegrees;
        double minutesDecimal = (totalDegrees - degrees) * 60;
        int minutes = (int) minutesDecimal;
        double secondsDecimal = (minutesDecimal - minutes) * 60;
        int seconds = (int) Math.round(secondsDecimal);

        return String.format("%d°%02d'%02d\"", degrees, minutes, seconds);
    }

    public boolean isWithinOrb(PlanetaryPosition other, double orbDegrees) {
        if (other == null || degree == null || other.getDegree() == null) {
            return false;
        }

        double difference = Math.abs(degree.doubleValue() - other.getDegree().doubleValue());
        // Handle the case where positions are across 0° (e.g., 358° and 2°)
        if (difference > 180) {
            difference = 360 - difference;
        }

        return difference <= orbDegrees;
    }

    public String getPlanetSymbol() {
        // Return astrological symbols for common planets
        switch (planet.toLowerCase()) {
            case "sun": case "surya": return "☉";
            case "moon": case "chandra": return "☽";
            case "mars": case "mangal": return "♂";
            case "mercury": case "budh": return "☿";
            case "jupiter": case "guru": case "brihaspati": return "♃";
            case "venus": case "shukra": return "♀";
            case "saturn": case "shani": return "♄";
            case "rahu": return "☊";
            case "ketu": return "☋";
            default: return planet.substring(0, 1).toUpperCase();
        }
    }
}
