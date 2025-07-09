package com.anterka.bjyotish.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanetaryPosition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BirthChart birthChart;

    private String planet;

    private String sign;

    private BigDecimal degree;

    private Integer house;

    @Builder.Default
    private Boolean retrograde = false;

    @Builder.Default
    private Instant createdAt = Instant.now();

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

    public String getFullDescription() {
        return String.format("%s is positioned in %s at %s degrees in the %s house%s",
                planet, sign, getDegreeFormatted(), getOrdinalHouse(),
                isRetrograde() ? " (Retrograde)" : "");
    }

    public String getDegreeFormatted() {
        return degree != null ? degree + "°" : "0°";
    }

    private String getOrdinalHouse() {
        if (house == null) return "unknown";
        return switch (house) {
            case 1 -> "1st";
            case 2 -> "2nd";
            case 3 -> "3rd";
            case 21, 22, 23 -> house + "rd";
            default -> house + "th";
        };
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
