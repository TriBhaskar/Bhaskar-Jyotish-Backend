package com.anterka.bjyotish.dto.users.request;

import lombok.Data;

@Data
public class UserForgotPasswordRequest {
    private String email;
    private String forgotPasswordLink;
}
