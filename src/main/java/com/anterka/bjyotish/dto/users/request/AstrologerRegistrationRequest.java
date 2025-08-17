package com.anterka.bjyotish.dto.users.request;

import com.anterka.bjyotish.constants.enums.UserRoleEnum;
import lombok.Data;

@Data
public class AstrologerRegistrationRequest extends UserRegistrationRequest {
    private String displayName;
//    private String bio;
//    private Integer yearsOfExperience;
//    private List<String> languagesSpoken;
//    private List<SpecializationTypeEnum> specializations;
//    private BigDecimal consultationFee;
//    private int consultationFeePerHour;
//    private int minimumConsultationDuration;

    @Override
    public UserRoleEnum getUserRole() {
        return UserRoleEnum.ASTROLOGER;
    }
    // getters and setters
}
