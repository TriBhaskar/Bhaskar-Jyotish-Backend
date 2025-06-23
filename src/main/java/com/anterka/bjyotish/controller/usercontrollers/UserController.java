package com.anterka.bjyotish.controller.usercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
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
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Enterprise registration endpoint is not implemented yet.");
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
