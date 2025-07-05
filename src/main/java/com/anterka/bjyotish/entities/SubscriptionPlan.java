package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.SubscriptionPlanTypeEnum;
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
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// SubscriptionPlan Entity
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription_plans")
public class SubscriptionPlan implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_plan_sequence_generator")
    @Column(name = "id")
    @SequenceGenerator(name = "subscription_plan_sequence_generator", sequenceName = "seq_subscription_plan_id", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "plan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SubscriptionPlanTypeEnum planType;

    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "features", columnDefinition = "text[]")
    private String[] features;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Bidirectional relationship with user subscriptions
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<UserSubscription> userSubscriptions = new ArrayList<>();

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
