package com.anterka.bjyotish.dto.users.request;

import lombok.Data;

@Data
public class UserResetPasswordRequest {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
