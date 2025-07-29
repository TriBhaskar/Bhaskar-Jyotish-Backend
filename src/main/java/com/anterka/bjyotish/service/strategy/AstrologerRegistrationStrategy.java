package com.anterka.bjyotish.service.strategy;

import com.anterka.bjyotish.constants.enums.SpecializationTypeEnum;
import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import com.anterka.bjyotish.constants.enums.UserStatusEnum;
import com.anterka.bjyotish.dao.AstrologerProfileRepository;
import com.anterka.bjyotish.dto.users.request.AstrologerRegistrationRequest;
import com.anterka.bjyotish.dto.users.request.UserRegistrationRequest;
import com.anterka.bjyotish.entities.AstrologerProfile;
import com.anterka.bjyotish.entities.BjyotishUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Astrologer registration strategy
@Component
@RequiredArgsConstructor
public class AstrologerRegistrationStrategy implements UserRegistrationStrategy {

    private final PasswordEncoder passwordEncoder;
    private final AstrologerProfileRepository astrologerProfileRepository;

    @Override
    public BjyotishUser createUser(UserRegistrationRequest request) {
        return BjyotishUser.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .role(UserRoleEnum.ASTROLOGER)
                .status(UserStatusEnum.PENDING_VERIFICATION) // Might need admin approval
                .emailVerified(false)
                .phoneVerified(false)
                .build();
    }

    @Override
    public void performPostRegistrationSetup(BjyotishUser user, UserRegistrationRequest request) {
        AstrologerRegistrationRequest astrologerRequest = (AstrologerRegistrationRequest) request;

        // Create astrologer profile
        AstrologerProfile profile = AstrologerProfile.builder()
                .bjyotishUser(user)
                .displayName(astrologerRequest.getDisplayName())
                .bio(astrologerRequest.getBio())
                .yearsOfExperience(astrologerRequest.getYearsOfExperience())
                .languagesSpoken(astrologerRequest.getLanguagesSpoken() != null ?
                        astrologerRequest.getLanguagesSpoken().toArray(new String[0]) : new String[0])
                .specializations(astrologerRequest.getSpecializations() != null ?
                        astrologerRequest.getSpecializations().toArray(new SpecializationTypeEnum[0]) : new SpecializationTypeEnum[0])
                .consultationFeePerHour(BigDecimal.valueOf(astrologerRequest.getConsultationFeePerHour()))
                .minimumConsultationDuration(astrologerRequest.getMinimumConsultationDuration())
                .isAvailableForConsultation(false) // Initially not available until profile is approved
                .verifiedAstrologer(false) // Requires admin verification
                .build();

        astrologerProfileRepository.save(profile);

        // Additional astrologer-specific setup can go here
        // For example: send admin notification for verification, create default availability slots, etc.
    }

    private void createAstrologerProfile(BjyotishUser user, AstrologerRegistrationRequest request) {
        AstrologerProfile profile = new AstrologerProfile();
        profile.setId(user.getId());
        profile.setDisplayName(request.getDisplayName());
        profile.setBio(request.getBio());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setConsultationFeePerHour(request.getConsultationFee());
        // Set other astrologer-specific fields
        astrologerProfileRepository.save(profile);
    }
}
