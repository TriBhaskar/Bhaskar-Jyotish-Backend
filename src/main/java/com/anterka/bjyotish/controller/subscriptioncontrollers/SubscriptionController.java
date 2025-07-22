package com.anterka.bjyotish.controller.subscriptioncontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.SubscriptionPlanDTO;
import com.anterka.bjyotish.entities.SubscriptionPlan;
import com.anterka.bjyotish.service.subscription.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_PREFIX)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubscriptionController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping(ApiPaths.SUBSCRIPTION_PLANS)
    public ResponseEntity<List<SubscriptionPlanDTO>> getSubscriptionPlans() {
        List<SubscriptionPlanDTO> plans = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(plans);
    }

    @GetMapping(ApiPaths.SUBSCRIPTION_PLAN_BY_ID)
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlanById(@PathVariable Long id) {
        return subscriptionPlanService.findPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
