package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.SupportUser;
import com.bot.middleware.persistence.dto.AwardDTO;
import com.bot.middleware.persistence.dto.SupportUserDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SupportUserMapper {
    public static SupportUserDTO createSupportUserDTOLazy(SupportUser supportUser) {
        SupportUserDTO supportUserDTO = SupportUserDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(supportUser.getPublicId()))
                .description(supportUser.getDescription())
                .relatedTo(supportUser.getRelatedTo())
                .userPublicId(PublicIdGenerator.encodedPublicId(supportUser.getUser().getPublicId()))
                .build();

        return supportUserDTO;
    }

    public static List<SupportUserDTO> createSupportUserDTOListLazy(Collection<SupportUser> supportUsers) {
        List<SupportUserDTO> supportUserDTOs = new ArrayList<>();
        supportUsers.forEach(supportUser -> supportUserDTOs.add(createSupportUserDTOLazy(supportUser)));
        return supportUserDTOs;
    }
}
