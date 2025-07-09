package com.anterka.bjyotish.dto.users.response;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.entities.BjyotishUserRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String accessToken;
    private String refreshToken;
    private Instant tokenExpiresAt;
    private UserRoleEnum role;
    private UserStatusEnum status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String profileImageUrl;

    // Static factory methods
    public static UserLoginResponse success(BjyotishUserRecord user, String accessToken,
                                            String refreshToken, Instant tokenExpiresAt) {
        return UserLoginResponse.builder()
                .userId(user.id())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenExpiresAt(tokenExpiresAt)
                .role(user.role())
                .status(user.status())
                .emailVerified(user.emailVerified())
                .phoneVerified(user.phoneVerified())
                .profileImageUrl(user.profileImageUrl())
                .build();
    }
}
