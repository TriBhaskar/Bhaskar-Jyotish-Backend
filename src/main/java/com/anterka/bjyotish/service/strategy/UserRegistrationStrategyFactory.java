package com.anterka.bjyotish.service.strategy;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Factory to get the right strategy
@Component
@RequiredArgsConstructor
public class UserRegistrationStrategyFactory {

    private final ClientRegistrationStrategy clientStrategy;
    private final AstrologerRegistrationStrategy astrologerStrategy;

    public UserRegistrationStrategy getStrategy(UserRoleEnum role) {
        return switch (role) {
            case CLIENT -> clientStrategy;
            case ASTROLOGER -> astrologerStrategy;
            case ADMIN, MODERATOR -> throw new UnsupportedOperationException(
                    "Registration strategy not implemented for role: " + role);
            default -> throw new IllegalArgumentException("Unknown user role: " + role);
        };
    }
}
