package com.anterka.bjyotish.dto.users.request;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class ClientRegistrationRequest extends UserRegistrationRequest {

    @Valid
    private BirthDetailsRequest birthDetails;

    @Override
    public UserRoleEnum getUserRole() {
        return UserRoleEnum.CLIENT;
    }

    // Check if birth details are provided
    public boolean hasBirthDetails() {
        return birthDetails != null;
    }
}
