package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.BjyotishUserRepository;
import com.anterka.bjyotish.dto.users.UserRegistrationRequest;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.exception.DataAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BjyotishAuthenticationService {

    private final BjyotishUserRepository bjyotishUserRepository;
    private final OtpService otpService;

    public void registerUser(UserRegistrationRequest request) {
        validateUserData(request);
        String otp = otpService.generateOtp();
        long otpValiditySeconds = otpService.saveOtp(request.getEmail(), otp);


    }

    /**
     * Accepts {@link UserRegistrationRequest}
     * - validates the email, name, contact number
     */
    private void validateUserData(UserRegistrationRequest request) {
        checkIfUserByEmailIsNew(request.getEmail());
        checkIfContactNumberAlreadyExists(request.getPhone());
    }

    /**
     * validates the user email
     *
     * @throws DataAlreadyExistsException if email already exists with some other user
     */
    private void checkIfUserByEmailIsNew(String email) {
        Optional<BjyotishUser> enterpriseByEmailAlreadyRegistered = bjyotishUserRepository.findByEmail(email);
        if (enterpriseByEmailAlreadyRegistered.isPresent()) {
            throw new DataAlreadyExistsException("Unable to register the user as email : [" + email + "] already exists");
        }
    }

    /**
     * validates the user contact number
     *
     * @throws DataAlreadyExistsException if contact number already associated with some other user
     */
    private void checkIfContactNumberAlreadyExists(String contactNumber) {
        Optional<BjyotishUser> contactNumberAlreadyRegistered = bjyotishUserRepository.findByPhone(contactNumber);
        if (contactNumberAlreadyRegistered.isPresent()) {
            throw new DataAlreadyExistsException("Unable to register the user as the contact number: [" + contactNumber + "] already registered with bjyotish");
        }
    }

}
