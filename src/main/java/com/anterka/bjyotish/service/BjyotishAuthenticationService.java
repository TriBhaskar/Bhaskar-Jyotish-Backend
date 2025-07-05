package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.BjyotishUserRepository;
import com.anterka.bjyotish.dto.users.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.UserRegistrationResponse;
import com.anterka.bjyotish.exception.DataAlreadyExistsException;
import com.anterka.bjyotish.exception.UserRegistrationException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
                        log.error("Failed to send OTP email: {}", throwable.getMessage());
                        return null;
                    });
        } catch (MessagingException e) {
            throw new UserRegistrationException("Failed to send OTP email"+ e.getMessage());
        }
        registrationCacheService.saveRegistration(request.getEmail(),request);
        return UserRegistrationResponse.success(
                null, // userId will be set after saving the user
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
        bjyotishUserRepository.findByEmailOrPhone(request.getEmail(), request.getPhone())
                .ifPresent(user -> {
                    if (user.getEmail().equals(request.getEmail())) {
                        throw new DataAlreadyExistsException("Email already exists: " + request.getEmail());
                    } else {
                        throw new DataAlreadyExistsException("Phone already exists: " + request.getPhone());
                    }
                });
    }

}
