package com.anterka.bjyotish.controller.usercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.users.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_PREFIX)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @PostMapping(ApiPaths.REGISTER)
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse success = UserRegistrationResponse.success(
                1L, // Placeholder for userId
                userRegistrationRequest.getEmail(),
                userRegistrationRequest.getFirstName(),
                userRegistrationRequest.getLastName(),
                3600L // Placeholder for OTP validity in seconds
        );
        return ResponseEntity.ok(success);
    }

    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Enterprise login endpoint is not implemented yet.");
    }

    @PostMapping(ApiPaths.VERIFY_OTP)
    public ResponseEntity<String> verifyOTP() {
        return ResponseEntity.ok("Enterprise OTP verification endpoint is not implemented yet.");
    }

    @PostMapping(ApiPaths.RESEND_OTP)
    public ResponseEntity<String> resendOTP() {
        return ResponseEntity.ok("Enterprise OTP resend endpoint is not implemented yet.");
    }

    @PostMapping(ApiPaths.FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword() {
        return ResponseEntity.ok("Enterprise forgot password endpoint is not implemented yet.");
    }

    @PostMapping(ApiPaths.VALIDATE_TOKEN)
    public ResponseEntity<String> validateToken(){
        return ResponseEntity.ok("Enterprise token validation endpoint is not implemented yet.");
    }

    @PostMapping(ApiPaths.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword() {
        return ResponseEntity.ok("Enterprise password reset endpoint is not implemented yet.");
    }
}
