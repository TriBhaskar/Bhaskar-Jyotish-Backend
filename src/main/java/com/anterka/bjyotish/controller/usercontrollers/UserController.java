package com.anterka.bjyotish.controller.usercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.CustomApiResponse;
import com.anterka.bjyotish.dto.ResponseStatusEnum;
import com.anterka.bjyotish.dto.users.request.*;
import com.anterka.bjyotish.dto.users.response.ResendOtpResponse;
import com.anterka.bjyotish.dto.users.response.UserLoginResponse;
import com.anterka.bjyotish.dto.users.response.UserRegistrationResponse;
import com.anterka.bjyotish.dto.users.response.UserTokenValidationResponse;
import com.anterka.bjyotish.exception.InvalidTokenException;
import com.anterka.bjyotish.exception.PasswordMismatchedException;
import com.anterka.bjyotish.exception.PasswordReusedException;
import com.anterka.bjyotish.exception.WeakPasswordException;
import com.anterka.bjyotish.service.BjyotishAuthenticationService;
import com.anterka.bjyotish.service.UserPasswordResetService;
import com.anterka.bjyotish.service.redis.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_V1_BASE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private static final Logger log = Logger.getLogger(UserController.class.getName());
    private final BjyotishAuthenticationService bjyotishAuthenticationService;
    private final RateLimiterService rateLimiter;
    private final UserPasswordResetService passwordResetService;

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpRequest) {
        log.info("Received login request for email: " + userLoginRequest.getEmail());
        return ResponseEntity.ok(bjyotishAuthenticationService.authenticateUser(userLoginRequest, httpRequest));
    }

    @PostMapping(ApiPaths.VERIFY_EMAIL)
    public ResponseEntity<CustomApiResponse> verifyEmail(@Valid @RequestBody UserEmailVerificationRequest userEmailVerificationRequest) {
        log.info("Received OTP verification request for email: " + userEmailVerificationRequest.getEmail());
        return ResponseEntity.ok(bjyotishAuthenticationService.verifyUserEmail(userEmailVerificationRequest));
    }

    @PostMapping(ApiPaths.RESEND_OTP)
    public ResponseEntity<ResendOtpResponse> resendOTP(@Valid @RequestBody UserResendOtpRequest userResendOtpRequest) {
        return ResponseEntity.ok(bjyotishAuthenticationService.resendOtp(userResendOtpRequest));
    }

    @PostMapping(ApiPaths.FORGOT_PASSWORD)
    public ResponseEntity<CustomApiResponse> forgotPassword(@Valid @RequestBody UserForgotPasswordRequest userForgotPasswordRequest,HttpServletRequest servletRequest) {
        log.info("Received forgot password request for email: " + userForgotPasswordRequest.getEmail());

        String clientIp = getClientIp(servletRequest);
        if (rateLimiter.isLimited("forgot_password", clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new CustomApiResponse("Too many requests. Please try again later.", ResponseStatusEnum.FAILED, LocalDateTime.now()));
        }
        try {
            passwordResetService.processForgotPassword(userForgotPasswordRequest);
            return ResponseEntity.ok().body(new CustomApiResponse("If your email is registered, you will receive a password reset link shortly", ResponseStatusEnum.SUCCESS, LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomApiResponse("If your email is registered, you will receive a password reset link shortly",ResponseStatusEnum.FAILED,LocalDateTime.now()));
        }
    }

    @PostMapping(ApiPaths.VALIDATE_RESET_TOKEN)
    public ResponseEntity<UserTokenValidationResponse> validateToken(@RequestBody String token, HttpServletRequest servletRequest) {
        // Rate limiting for token validation
        String clientIp = getClientIp(servletRequest);
        if (rateLimiter.isLimited("validate_token", clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new UserTokenValidationResponse(false, "Too many attempts. Please try again later."));
        }

        UserTokenValidationResponse result = passwordResetService.validateToken(token);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(ApiPaths.RESET_PASSWORD)
    public ResponseEntity<CustomApiResponse> resetPassword(@RequestBody UserResetPasswordRequest request,
                                                HttpServletRequest servletRequest) {
        // Rate limiting for reset password attempts
        String clientIp = getClientIp(servletRequest);
        if (rateLimiter.isLimited("reset_password", clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new CustomApiResponse( "Too many attempts. Please try again later.",ResponseStatusEnum.FAILED,LocalDateTime.now()));
        }
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok().body(new CustomApiResponse("Password reset successful",ResponseStatusEnum.SUCCESS,LocalDateTime.now()));
        } catch (InvalidTokenException | PasswordMismatchedException |
                 WeakPasswordException | PasswordReusedException e) {
            return ResponseEntity.badRequest().body(new CustomApiResponse( "Exception occurred while resetting password : "+ e.getMessage(),ResponseStatusEnum.FAILED,LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomApiResponse("An unexpected error occurred. Please try again.",ResponseStatusEnum.FAILED,LocalDateTime.now()));
        }
    }

    @PostMapping(ApiPaths.DELETE_ACCOUNT)
    public ResponseEntity<String> deleteAccount() {
        return ResponseEntity.ok("Enterprise account deletion endpoint is not implemented yet.");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    //for redis testing purpose
    @PostMapping("v1/testredis")
    public ResponseEntity<String> testRedis(@RequestBody UserRegistrationRequest request) {
        log.info("Received test redis request for email: " + request.getEmail());
        return ResponseEntity.ok(request.toString());
    }
}
