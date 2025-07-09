package com.anterka.bjyotish.entities;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;

import java.time.Instant;
import java.time.LocalDate;

public record BjyotishUserRecord(
        Long id,
        String email,
        String phone,
        String passwordHash,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        UserRoleEnum role,
        UserStatusEnum status,
        String profileImageUrl,
        Boolean emailVerified,
        Boolean phoneVerified,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt
) {}
