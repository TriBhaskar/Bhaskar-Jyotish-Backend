package com.anterka.bjyotish.controller.usercontrollers;

import com.anterka.bjyotish.controller.constants.ApiPaths;
import com.anterka.bjyotish.dto.CustomApiResponse;
import com.anterka.bjyotish.dto.users.request.UserAddressRequest;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.response.UserAddressResponse;
import com.anterka.bjyotish.dto.users.response.UserRegistrationResponse;
import com.anterka.bjyotish.service.BjyotishAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_PREFIX )
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserAddressController {

    private static final Logger log = Logger.getLogger(UserAddressController.class.getName());

//    @PostMapping(ApiPaths.USER_ADDRESSES)
//    public ResponseEntity<UserAddressResponse> createAddress(@Valid @RequestBody UserAddressRequest request, Authentication authentication) {
//        log.info("Received request to create user address");
//        return ResponseEntity.status(HttpStatus.CREATED).body(userAddressService.createAddress(request,authentication.getPrincipal().toString()));
//    }
//
//    @GetMapping(ApiPaths.USER_ADDRESSES)
//    public ResponseEntity<List<UserAddressResponse>> getAllAddresses() {
//        log.info("Received request to get all user addresses");
//        return ResponseEntity.ok(userAddressService.getAllAddresses());
//    }
//
//    @GetMapping(ApiPaths.USER_ADDRESS_BY_ID)
//    public ResponseEntity<UserAddressResponse> getAddressById(@PathVariable("id") Long id) {
//        log.info("Received request to get user address with ID: " + id);
//        return ResponseEntity.ok(userAddressService.getAddressById(id));
//    }
//
//    @PutMapping(ApiPaths.USER_ADDRESS_BY_ID)
//    public ResponseEntity<UserAddressResponse> updateAddress(
//            @PathVariable("id") Long id,
//            @Valid @RequestBody UserAddressRequest request) {
//        log.info("Received request to update user address with ID: " + id);
//        return ResponseEntity.ok(userAddressService.updateAddress(id, request));
//    }
//
//    @DeleteMapping(ApiPaths.USER_ADDRESS_BY_ID)
//    public ResponseEntity<CustomApiResponse> deleteAddress(@PathVariable("id") Long id) {
//        log.info("Received request to delete user address with ID: " + id);
//        return ResponseEntity.ok(userAddressService.deleteAddress(id));
//    }
//
//    @PutMapping(ApiPaths.SET_PRIMARY_ADDRESS)
//    public ResponseEntity<CustomApiResponse> setPrimaryAddress(@PathVariable("id") Long id) {
//        log.info("Received request to set primary address with ID: " + id);
//        return ResponseEntity.ok(userAddressService.setPrimaryAddress(id));
//    }
}
