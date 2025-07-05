package com.anterka.bjyotish.security;

import com.anterka.bjyotish.controller.constants.ApiPaths;

public class AuthorizationPaths {
    public static final String[] SKIP_AUTHORIZATION_REQUESTS = {
            ApiPaths.API_PREFIX+ApiPaths.LOGIN,
            ApiPaths.API_PREFIX+ApiPaths.REGISTER,
            ApiPaths.API_PREFIX+ApiPaths.VERIFY_EMAIL,
            ApiPaths.API_PREFIX+ApiPaths.FORGOT_PASSWORD,
            ApiPaths.API_PREFIX+ApiPaths.VALIDATE_TOKEN,
            ApiPaths.API_PREFIX+ApiPaths.RESET_PASSWORD,
            ApiPaths.API_PREFIX+ApiPaths.RESEND_OTP,
            "/api/v1/testredis"
    };
}
