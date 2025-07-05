package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.BjyotishUserRepository;
import com.anterka.bjyotish.dto.users.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.UserRegistrationResponse;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.exception.DataAlreadyExistsException;
import com.anterka.bjyotish.exception.UserRegistrationException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BjyotishAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(BjyotishAuthenticationService.class);
    private final BjyotishUserRepository bjyotishUserRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RegistrationCacheService registrationCacheService;

    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        validateUserData(request);
        String otp = otpService.generateOtp();
        long otpValiditySeconds = otpService.saveOtp(request.getEmail(), otp);
        try {
            // Async email sending
            emailService.sendOTPMail(request.getEmail(), otp)
                    .exceptionally(throwable -> {
                        // Log the error but don't block the registration
                        log.error("Failed to send OTP email: " + throwable.getMessage());
                        return null;
                    });
        } catch (MessagingException e) {
            throw new UserRegistrationException("Failed to send OTP email"+ e.getMessage());
        }
        registrationCacheService.saveRegistration(request.getEmail(),request);
        return UserRegistrationResponse.success(
                123L, // userId will be set after saving the user
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                otpValiditySeconds
        );
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
