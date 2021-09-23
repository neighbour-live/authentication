package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.dto.UserCertificationsDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserCertificationsMapper {

    public static UserCertificationsDTO createUserCertificationsDTOLazy(UserCertification userCertifications) {
        UserCertificationsDTO userCertificationsDTO = UserCertificationsDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userCertifications.getPublicId()))
                .certificationURL(userCertifications.getCertificationURL())
                .expiryDate(userCertifications.getExpiryDate())
                .issuingDate(userCertifications.getIssuingDate())
                .issuingInstitution(userCertifications.getIssuingInstitution())
                .title(userCertifications.getTitle())
                .expiryDate(userCertifications.getExpiryDate())
                .description(userCertifications.getDescription())
                .userPublicId(PublicIdGenerator.encodedPublicId(userCertifications.getUser().getPublicId()))
                .build();

        return userCertificationsDTO;
    }

    public static List<UserCertificationsDTO> createUserCertificationsDTOListLazy(Collection<UserCertification> userCertifications) {
        List<UserCertificationsDTO> userCertificationsDTOS = new ArrayList<>();
        userCertifications.forEach(userCertification -> userCertificationsDTOS.add(createUserCertificationsDTOLazy(userCertification)));
        return userCertificationsDTOS;
    }
}
