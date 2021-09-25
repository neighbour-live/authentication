package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.UserAward;
import com.app.middleware.persistence.dto.UserAwardsDTO;
import com.app.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAwardsMapper {

    public static UserAwardsDTO createUserAwardsDTOLazy(UserAward userAward) {
        UserAwardsDTO userAwardsDTO = UserAwardsDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(userAward.getPublicId()))
                .isActive(userAward.getIsActive())
                .isUnlocked(userAward.getIsUnlocked())
                .progress(userAward.getProgress())
                .userPublicId(PublicIdGenerator.encodedPublicId(userAward.getUser().getPublicId()))
                .award(AwardMapper.createAwardDTOLazy(userAward.getAward()))
                .build();

        return userAwardsDTO;
    }

    public static List<UserAwardsDTO> createUserAwardsDTOListLazy(Collection<UserAward> userAwards) {
        List<UserAwardsDTO> awardDTOS = new ArrayList<>();
        userAwards.forEach(award -> awardDTOS.add(createUserAwardsDTOLazy(award)));
        return awardDTOS;
    }
}
