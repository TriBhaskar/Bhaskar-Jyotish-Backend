package com.anterka.bjyotish.controller.astrologercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.users.request.AstrologerRegistrationRequest;
import com.anterka.bjyotish.dto.users.response.UserRegistrationResponse;
import com.anterka.bjyotish.service.BjyotishAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_V1_BASE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AstrologerController {

    private final BjyotishAuthenticationService bjyotishAuthenticationService;

    @PostMapping(ApiPaths.ASTROLOGER_REGISTER)
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody AstrologerRegistrationRequest astrologerRegistrationRequest) {
        log.info("Received registration request for email: " + astrologerRegistrationRequest.getEmail());
        return ResponseEntity.ok(bjyotishAuthenticationService.registerAstrologer(astrologerRegistrationRequest));
    }
}
