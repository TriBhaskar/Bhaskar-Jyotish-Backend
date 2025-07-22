package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    // Additional query methods can be defined here if needed
    Optional<SubscriptionPlan> findByName(String name);
    Optional<SubscriptionPlan> findById(Long id);
    @Override
    List<SubscriptionPlan> findAll();
    // Custom query to fetch only necessary fields
    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.isActive = true")
    List<SubscriptionPlan> findAllActive();
}
