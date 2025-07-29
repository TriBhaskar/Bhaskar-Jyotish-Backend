package com.anterka.bjyotish.mapper;


import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.entities.BjyotishUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public BjyotishUser toEntityCustomer(UserRegistrationRequest request) {
        return BjyotishUser.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRoleEnum.CLIENT)
                .status(UserStatusEnum.PENDING_VERIFICATION)
                .emailVerified(true)
                .phoneVerified(false)
                .build();
    }

    public BjyotishUser toEntityAstrologer(UserRegistrationRequest request) {
        return BjyotishUser.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getUserRole())
                .status(UserStatusEnum.PENDING_VERIFICATION)
                .emailVerified(true)
                .phoneVerified(false)
                .build();
    }
}
