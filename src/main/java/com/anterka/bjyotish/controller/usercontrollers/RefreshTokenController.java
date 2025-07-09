package com.anterka.bjyotish.controller.usercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.users.request.RefreshTokenRequest;
import com.anterka.bjyotish.dto.users.response.UserLoginResponse;
import com.anterka.bjyotish.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiPaths.API_PREFIX)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    /**
     * Endpoint to refresh access token using refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<UserLoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {

        log.info("Received refresh token request");

        Optional<RefreshTokenService.TokenRefreshResult> result =
                refreshTokenService.refreshAccessToken(request.getRefreshToken(), httpRequest);

        if (result.isEmpty()) {
            log.warn("Invalid refresh token provided");
            return ResponseEntity.badRequest().build();
        }

        RefreshTokenService.TokenRefreshResult tokenResult = result.get();

        UserLoginResponse response = UserLoginResponse.success(
                tokenResult.getUserRecord(),
                tokenResult.getAccessToken(),
                tokenResult.getRefreshToken(),
                tokenResult.getExpiresAt()
        );

        log.info("Successfully refreshed token for user: {}", tokenResult.getUserRecord().email());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to revoke refresh token (logout)
     */
    @PostMapping("/revoke")
    public ResponseEntity<Void> revokeToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Received revoke token request");

        boolean revoked = refreshTokenService.revokeRefreshToken(request.getRefreshToken());

        if (revoked) {
            log.info("Successfully revoked refresh token");
            return ResponseEntity.ok().build();
        } else {
            log.warn("Failed to revoke refresh token - token not found or already revoked");
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint to revoke all refresh tokens for a user (logout from all devices)
     */
    @PostMapping("/revoke-all")
    public ResponseEntity<Void> revokeAllTokens(@RequestParam Long userId) {
        log.info("Received revoke all tokens request for user: {}", userId);

        refreshTokenService.revokeAllRefreshTokensForUser(userId);

        log.info("Successfully revoked all tokens for user: {}", userId);
        return ResponseEntity.ok().build();
    }
}
