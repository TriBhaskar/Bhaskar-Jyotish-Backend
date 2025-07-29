package com.anterka.bjyotish.service.strategy;

import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.entities.BjyotishUser;

// Base strategy interface
public interface UserRegistrationStrategy {
    BjyotishUser createUser(UserRegistrationRequest request);
    void performPostRegistrationSetup(BjyotishUser user, UserRegistrationRequest request);
}