package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.BjyotishUserRepository;
import com.anterka.bjyotish.dto.CustomApiResponse;
import com.anterka.bjyotish.dto.ResponseStatusEnum;
import com.anterka.bjyotish.dto.users.request.UserEmailVerificationRequest;
import com.anterka.bjyotish.dto.users.request.UserLoginRequest;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.dto.users.request.UserResendOtpRequest;
import com.anterka.bjyotish.dto.users.response.ResendOtpResponse;
import com.anterka.bjyotish.dto.users.response.UserLoginResponse;
import com.anterka.bjyotish.dto.users.response.UserRegistrationResponse;
import com.anterka.bjyotish.entities.BjyotishUser;
import com.anterka.bjyotish.entities.UserSession;
import com.anterka.bjyotish.exception.CredentialValidationException;
import com.anterka.bjyotish.exception.DataAlreadyExistsException;
import com.anterka.bjyotish.exception.UserAuthenticationException;
import com.anterka.bjyotish.exception.UserRegistrationException;
import com.anterka.bjyotish.mapper.UserRegistrationMapper;
import com.anterka.bjyotish.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BjyotishAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(BjyotishAuthenticationService.class);
    private final BjyotishUserRepository bjyotishUserRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RegistrationCacheService registrationCacheService;
    private final UserRegistrationMapper userRegistrationMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

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

    @Transactional
    public UserLoginResponse authenticateUser(UserLoginRequest request, HttpServletRequest httpRequest){
        Instant startTime1 = Instant.now();
        // Fetch user first to avoid duplicate queries
        BjyotishUser bjyotishUser = bjyotishUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserAuthenticationException("User not found"));
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }catch (BadCredentialsException ex) {
            throw new CredentialValidationException("Invalid email or password for user: [" + request.getEmail() + "]");
        } catch (LockedException ex) {
            throw new UserAuthenticationException("User with email: [" + request.getEmail() + "] is blocked, please contact the clos-auth");
        } catch (Exception ex) {
            throw new UserAuthenticationException("Exception occurred while authenticating the user: [" + request.getEmail() + "], Error: " + ex.getMessage());
        }
        log.info("User authenticated successfully!!");
        Instant endTime1 = Instant.now();
        log.info("Authentication time: {} ms", endTime1.toEpochMilli() - startTime1.toEpochMilli());

        Instant startTime2 = Instant.now();

        CompletableFuture<String> jwtFuture = CompletableFuture.supplyAsync(() ->
                jwtUtils.generateJwtToken(bjyotishUser.getEmail())
        );
        Instant expirationTime = Instant.ofEpochMilli(jwtUtils.getJwtExpirationTimeInMillis());
        Instant endTime2 = Instant.now();
        log.info("Jwt generation time: {} ms", endTime2.toEpochMilli() - startTime2.toEpochMilli());

        // Create user session with refresh token
        Instant startTime3 = Instant.now();
        CompletableFuture<UserSession> sessionFuture = jwtFuture.thenApplyAsync(jwt ->
                refreshTokenService.createSession(bjyotishUser, jwt, httpRequest)
        );

        // Wait for both operations to complete
        String jwtToken = jwtFuture.join();
        UserSession userSession = sessionFuture.join();
        Instant endTime3 = Instant.now();
        log.info("User session creation time: {} ms", endTime3.toEpochMilli() - startTime3.toEpochMilli());
        return UserLoginResponse.success(bjyotishUser,jwtToken,userSession.getRefreshToken(),expirationTime);
    }

    @Transactional
    public CustomApiResponse verifyUserEmail(UserEmailVerificationRequest request) {
        log.info("Verifying user email: {}", request.getEmail());
        registrationCacheService.getRegistration(request.getEmail())
                .ifPresentOrElse(registration -> {
                    String cachedOtp = otpService.getOtp(request.getEmail());
                    if (cachedOtp == null || !cachedOtp.equals(request.getOtp())) {
                        throw new UserRegistrationException("Invalid OTP for email: " + request.getEmail());
                    }else{
                        BjyotishUser user = userRegistrationMapper.toEntity(registration);
                        user.setEmailVerified(true);
                        bjyotishUserRepository.save(user);
                        registrationCacheService.deleteRegistration(request.getEmail());
                        otpService.deleteOtp(request.getEmail());
                        log.info("User email verified successfully: {}", request.getEmail());
                    }
                }, () -> {
                    throw new UserRegistrationException("No registration found for email: " + request.getEmail());
                });
        return CustomApiResponse.builder()
                .status(ResponseStatusEnum.SUCCESS)
                .message("User registered successfully")
                .timestamp(LocalDateTime.now()).build();
    }

    @Transactional
    public ResendOtpResponse resendOtp(UserResendOtpRequest request) {

        if(registrationCacheService.registrationExists(request.getEmail())){
            String otp = otpService.generateOtp();
            otpService.saveOtp(request.getEmail(), otp);
            try {
                // Async email sending
                emailService.sendOTPMail(request.getEmail(), otp)
                        .exceptionally(throwable -> {
                            // Log the error but don't block the resend
                            log.error("Failed to send OTP email: {}", throwable.getMessage());
                            return null;
                        });
            } catch (MessagingException e) {
                throw new UserRegistrationException("Failed to send OTP email"+ e.getMessage());
            }
        }else {
            throw new UserRegistrationException("No registration found for email: " + request.getEmail());
        }

        return ResendOtpResponse.builder()
                .email(request.getEmail())
                .otpValiditySeconds(OtpService.OTP_VALIDITY_SECONDS)
                .message("OTP resent successfully please verify your email to activate your account")
                .timestamp(LocalDateTime.now())
                .build();
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
