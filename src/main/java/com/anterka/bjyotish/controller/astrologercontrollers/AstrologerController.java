package com.anterka.bjyotish.controller.astrologercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.response.UserRegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_V1_BASE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AstrologerController {

    @PostMapping(ApiPaths.ASTROLOGER_REGISTER)
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("Received registration request for email: " + userRegistrationRequest.getEmail());
        return ResponseEntity.ok(bjyotishAuthenticationService.registerUser(userRegistrationRequest));
    }
}
