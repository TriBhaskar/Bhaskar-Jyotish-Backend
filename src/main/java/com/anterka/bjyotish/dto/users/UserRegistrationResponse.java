package com.anterka.bjyotish.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private long otpValiditySeconds;
    private LocalDateTime timestamp;

    public static UserRegistrationResponse success(Long userId, String email, String firstName, String lastName, long otpValiditySeconds) {
        return UserRegistrationResponse.builder()
                .userId(userId)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .message("Registration successful. Please verify your email to activate your account.")
                .otpValiditySeconds(otpValiditySeconds)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
