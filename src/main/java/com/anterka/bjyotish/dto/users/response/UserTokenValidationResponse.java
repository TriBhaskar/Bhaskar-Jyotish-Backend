package com.anterka.bjyotish.dto.users.response;

import lombok.Data;

@Data
public class UserTokenValidationResponse {
    private final boolean valid;
    private final String message;
}
