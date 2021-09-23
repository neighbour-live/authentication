package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.dto.AwardDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AwardMapper {

    public static AwardDTO createAwardDTOLazy(Award award) {
        AwardDTO awardDTO = AwardDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(award.getPublicId()))
                .awardIcon(award.getAwardIcon())
                .awardType(award.getAwardType().toString())
                .description(award.getDescription())
                .title(award.getTitle())
                .build();

        return awardDTO;
    }

    public static List<AwardDTO> createAwardDTOListLazy(Collection<Award> awards) {
        List<AwardDTO> awardDTOS = new ArrayList<>();
        awards.forEach(award -> awardDTOS.add(createAwardDTOLazy(award)));
        return awardDTOS;
    }
}
