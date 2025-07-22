package com.anterka.bjyotish.dto;

import com.anterka.bjyotish.constants.enums.SubscriptionPlanTypeEnum;
import com.anterka.bjyotish.entities.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanDTO {
    private Long id;
    private String name;
    private String description;
    private SubscriptionPlanTypeEnum planType;
    private Integer durationMonths;
    private BigDecimal price;
    private List<String> features;
    private BigDecimal monthlyPrice;

    // Constructor from entity
    public SubscriptionPlanDTO(SubscriptionPlan plan) {
        this.id = plan.getId();
        this.name = plan.getName();
        this.description = plan.getDescription();
        this.planType = plan.getPlanType();
        this.durationMonths = plan.getDurationMonths();
        this.price = plan.getPrice();
        this.features = plan.getFeaturesList();
        this.monthlyPrice = plan.getMonthlyPrice();
    }
}
