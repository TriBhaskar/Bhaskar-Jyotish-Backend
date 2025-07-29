package com.anterka.bjyotish.dto.users.request;

import com.anterka.bjyotish.entities.BirthDetails;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for birth details used in astrological calculations
 * Maps to BirthDetails entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BirthDetailsRequest {

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Birth time is required")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime birthTime;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @NotNull(message = "Birth latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
    @Digits(integer = 2, fraction = 8, message = "Latitude must have maximum 2 integer digits and 8 decimal places")
    private BigDecimal birthLatitude;

    @NotNull(message = "Birth longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
    @Digits(integer = 3, fraction = 8, message = "Longitude must have maximum 3 integer digits and 8 decimal places")
    private BigDecimal birthLongitude;

    @NotBlank(message = "Timezone is required")
    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9/_+-]+$", message = "Invalid timezone format")
    private String timezone;

    @Builder.Default
    private Boolean isBirthTimeAccurate = true;

    // Additional validation methods

    /**
     * Validates if the provided coordinates are valid geographical coordinates
     */
    public boolean areCoordinatesValid() {
        if (birthLatitude == null || birthLongitude == null) {
            return false;
        }

        return birthLatitude.compareTo(new BigDecimal("-90")) >= 0 &&
                birthLatitude.compareTo(new BigDecimal("90")) <= 0 &&
                birthLongitude.compareTo(new BigDecimal("-180")) >= 0 &&
                birthLongitude.compareTo(new BigDecimal("180")) <= 0;
    }

    /**
     * Validates if the birth date and time combination is reasonable
     */
    public boolean isDateTimeReasonable() {
        if (birthDate == null) {
            return false;
        }

        // Birth date should not be more than 150 years ago
        LocalDate minDate = LocalDate.now().minusYears(150);

        // Birth date should not be in the future
        LocalDate maxDate = LocalDate.now();

        return !birthDate.isBefore(minDate) && !birthDate.isAfter(maxDate);
    }

    /**
     * Returns a formatted string representation of birth location
     */
    public String getFormattedLocation() {
        if (city == null || state == null || country == null ||
                birthLatitude == null || birthLongitude == null) {
            return null;
        }

        String birthPlace = String.format("%s, %s, %s", city, state, country);

        return String.format("%s (%.6f°, %.6f°)",
                birthPlace,
                birthLatitude.doubleValue(),
                birthLongitude.doubleValue());
    }

    /**
     * Returns a formatted string representation of birth date and time
     */
    public String getFormattedDateTime() {
        if (birthDate == null || birthTime == null) {
            return null;
        }

        return String.format("%s at %s %s",
                birthDate.toString(),
                birthTime.toString(),
                isBirthTimeAccurate ? "(Accurate)" : "(Approximate)");
    }

    /**
     * Creates a BirthDetails entity from this request DTO
     */
    public BirthDetails toEntity(BjyotishUser user) {
        return BirthDetails.builder()
                .bjyotishUser(user)
                .birthDate(this.birthDate)
                .birthTime(this.birthTime)
                .city(this.city)
                .state(this.state)
                .country(this.country)
                .postalCode(this.postalCode)
                .birthLatitude(this.birthLatitude)
                .birthLongitude(this.birthLongitude)
                .timezone(this.timezone)
                .isBirthTimeAccurate(this.isBirthTimeAccurate)
                .build();
    }
}
