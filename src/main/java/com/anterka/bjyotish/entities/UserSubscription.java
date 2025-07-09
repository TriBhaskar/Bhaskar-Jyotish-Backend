package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.SubscriptionStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// UserSubscription Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscription implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private BjyotishUser bjyotishUser;

    private SubscriptionPlan plan;

    @Builder.Default
    private SubscriptionStatusEnum status = SubscriptionStatusEnum.ACTIVE;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean autoRenewal = true;

    private String paymentMethod;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Utility methods
    public boolean isActive() {
        return status == SubscriptionStatusEnum.ACTIVE &&
                !endDate.isBefore(LocalDate.now());
    }

    public boolean isExpired() {
        return status == SubscriptionStatusEnum.EXPIRED ||
                endDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon(int daysThreshold) {
        return isActive() &&
                endDate.isBefore(LocalDate.now().plusDays(daysThreshold));
    }

    public long getDaysRemaining() {
        if (isExpired()) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public long getDaysUsed() {
        LocalDate currentDate = LocalDate.now();
        LocalDate comparisonDate = currentDate.isBefore(endDate) ? currentDate : endDate;
        return ChronoUnit.DAYS.between(startDate, comparisonDate);
    }

    public long getTotalDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public double getUsagePercentage() {
        long totalDays = getTotalDays();
        if (totalDays == 0) return 0;
        return (double) getDaysUsed() / totalDays * 100;
    }

    public LocalDate getNextRenewalDate() {
        if (!autoRenewal) return null;
        return endDate.plusDays(1);
    }

    public UserSubscription renew() {
        if (!autoRenewal) {
            throw new IllegalStateException("Auto-renewal is disabled");
        }

        return UserSubscription.builder()
                .bjyotishUser(bjyotishUser)
                .plan(plan)
                .status(SubscriptionStatusEnum.ACTIVE)
                .startDate(endDate.plusDays(1))
                .endDate(endDate.plusMonths(plan.getDurationMonths()))
                .autoRenewal(autoRenewal)
                .paymentMethod(paymentMethod)
                .build();
    }

    public void cancel() {
        this.status = SubscriptionStatusEnum.CANCELLED;
        this.autoRenewal = false;
    }

    public void pause() {
        if (status != SubscriptionStatusEnum.ACTIVE) {
            throw new IllegalStateException("Can only pause active subscriptions");
        }
        this.status = SubscriptionStatusEnum.PAUSED;
    }

    public void resume() {
        if (status != SubscriptionStatusEnum.PAUSED) {
            throw new IllegalStateException("Can only resume paused subscriptions");
        }
        this.status = SubscriptionStatusEnum.ACTIVE;
    }
}
