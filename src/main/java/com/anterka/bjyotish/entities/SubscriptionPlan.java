package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.SubscriptionPlanTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// SubscriptionPlan Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlan implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private SubscriptionPlanTypeEnum planType;

    private Integer durationMonths;

    private BigDecimal price;

    private String[] features;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Bidirectional relationship with user subscriptions
    private List<UserSubscription> userSubscriptions = new ArrayList<>();

    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public BigDecimal getMonthlyPrice() {
        return price.divide(BigDecimal.valueOf(durationMonths), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscountPercentage(SubscriptionPlan monthlyPlan) {
        if (monthlyPlan.getDurationMonths() != 1) {
            throw new IllegalArgumentException("Comparison plan must be monthly");
        }

        BigDecimal monthlyTotal = monthlyPlan.getPrice().multiply(BigDecimal.valueOf(durationMonths));
        BigDecimal savings = monthlyTotal.subtract(price);

        return savings.divide(monthlyTotal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public boolean hasFeature(String feature) {
        if (features == null) return false;
        return Arrays.asList(features).contains(feature);
    }

    public List<String> getFeaturesList() {
        return features == null ? new ArrayList<>() : Arrays.asList(features);
    }
}
