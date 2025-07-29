package com.anterka.bjyotish.security;

import com.anterka.bjyotish.controller.constants.ApiPaths;

public class AuthorizationPaths {
    public static final String[] SKIP_AUTHORIZATION_REQUESTS = {
            ApiPaths.API_V1_BASE+ApiPaths.LOGIN,
            ApiPaths.API_V1_BASE+ApiPaths.CUSTOMER_REGISTER,
            ApiPaths.API_V1_BASE+ApiPaths.ASTROLOGER_REGISTER,
            ApiPaths.API_V1_BASE+ApiPaths.VERIFY_EMAIL,
            ApiPaths.API_V1_BASE+ApiPaths.FORGOT_PASSWORD,
            ApiPaths.API_V1_BASE+ApiPaths.VALIDATE_RESET_TOKEN,
            ApiPaths.API_V1_BASE+ApiPaths.RESET_PASSWORD,
            ApiPaths.API_V1_BASE+ApiPaths.RESEND_OTP,
            "/api/v1/testredis"
    };
}
