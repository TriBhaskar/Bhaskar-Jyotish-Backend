package com.anterka.bjyotish.dto.users.request;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public abstract class UserRegistrationRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String lastName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(male|female|other)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Gender must be 'male', 'female', or 'other'")
    private String gender;

    // Accept terms and conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean acceptTerms;

    public abstract UserRoleEnum getUserRole();
}
