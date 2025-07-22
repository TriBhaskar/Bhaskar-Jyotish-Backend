package com.anterka.bjyotish.service.subscription;

import com.anterka.bjyotish.dao.SubscriptionPlanRepository;
import com.anterka.bjyotish.dto.SubscriptionPlanDTO;
import com.anterka.bjyotish.entities.SubscriptionPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .filter(SubscriptionPlan::getIsActive) // Only active plans
                .map(SubscriptionPlanDTO::new)
                .collect(Collectors.toList());
    }

    public List<SubscriptionPlanDTO> findAllPlansIncludingInactive() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(SubscriptionPlanDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<SubscriptionPlanDTO> findPlanById(Long id) {
        return subscriptionPlanRepository.findById(id)
                .map(SubscriptionPlanDTO::new);
    }

    public Optional<SubscriptionPlanDTO> findPlanByName(String name) {
        return subscriptionPlanRepository.findByName(name)
                .map(SubscriptionPlanDTO::new);
    }
}
