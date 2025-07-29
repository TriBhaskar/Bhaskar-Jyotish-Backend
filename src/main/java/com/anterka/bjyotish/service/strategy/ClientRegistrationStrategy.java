package com.anterka.bjyotish.service.strategy;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;
import com.anterka.bjyotish.dao.BirthDetailsRepository;
import com.anterka.bjyotish.dto.users.request.BirthDetailsRequest;
import com.anterka.bjyotish.dto.users.request.ClientRegistrationRequest;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.entities.BjyotishUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientRegistrationStrategy implements UserRegistrationStrategy {

    private final PasswordEncoder passwordEncoder;
    private final BirthDetailsRepository birthDetailsRepository;

    @Override
    public BjyotishUser createUser(UserRegistrationRequest request) {
        return BjyotishUser.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .role(UserRoleEnum.CLIENT)
                .status(UserStatusEnum.PENDING_VERIFICATION)
                .emailVerified(false)
                .phoneVerified(false)
                .build();
    }

    @Override
    public void performPostRegistrationSetup(BjyotishUser user, UserRegistrationRequest request) {
        ClientRegistrationRequest clientRequest = (ClientRegistrationRequest) request;

        // Only create birth details if provided
        if (clientRequest.hasBirthDetails()) {
            createBirthDetails(user, clientRequest.getBirthDetails());
        }

        // You could also create a profile completion reminder/flag
        if (!clientRequest.hasBirthDetails()) {
            // Mark profile as incomplete for future prompting
            // or send email reminder to complete profile
        }
    }

    private void createBirthDetails(BjyotishUser user, BirthDetailsRequest birthDetails) {
        // Implementation for creating birth details
    }
}

