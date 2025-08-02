package com.anterka.bjyotish.controller.clientcontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.users.request.ClientRegistrationRequest;
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
public class ClientController {

    private final BjyotishAuthenticationService bjyotishAuthenticationService;

    @PostMapping(ApiPaths.CUSTOMER_REGISTER)
    public ResponseEntity<UserRegistrationResponse> registerClient(@Valid @RequestBody ClientRegistrationRequest clientRegistrationRequest) {
        log.info("Received registration request for email: " + clientRegistrationRequest.getEmail());
        return ResponseEntity.ok(bjyotishAuthenticationService.registerClient(clientRegistrationRequest));
    }
}
