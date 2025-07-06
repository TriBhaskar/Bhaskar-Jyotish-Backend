package com.anterka.bjyotish.dto.users.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpResponse {
    private String message;
    private long otpValiditySeconds;
    private String email;
    private LocalDateTime timestamp;
}
