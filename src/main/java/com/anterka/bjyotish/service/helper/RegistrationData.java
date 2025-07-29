package com.anterka.bjyotish.service.helper;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationData {
    private UserRegistrationRequest userRegistrationRequest;
    private UserRoleEnum userRole;
}
