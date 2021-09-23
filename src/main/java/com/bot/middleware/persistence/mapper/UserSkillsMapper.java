package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.UserSkill;
import com.bot.middleware.persistence.dto.SkillDTO;
import com.bot.middleware.persistence.dto.UserSkillsDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserSkillsMapper {

    public static UserSkillsDTO createUserSkillsDTOLazy(UserSkill userSkill) {
        UserSkillsDTO userSkillsDTO = UserSkillsDTO.builder()
                .isActive(userSkill.getIsActive())
                .skill(SkillMapper.createSkillDTOLazy(userSkill.getSkill()))
                .userPublicId(PublicIdGenerator.encodedPublicId(userSkill.getUser().getPublicId()))
                .publicId(PublicIdGenerator.encodedPublicId(userSkill.getPublicId()))
                .skillProficiency(userSkill.getSkillProficiency().toString())
                .title(userSkill.getTitle())
                .description(userSkill.getDescription())
                .build();

        return userSkillsDTO;
    }

    public static List<UserSkillsDTO> createUserSkillsDTOListLazy(Collection<UserSkill> userSkills) {
        List<UserSkillsDTO> userSkillsDTOS = new ArrayList<>();
        userSkills.forEach(userSkill -> userSkillsDTOS.add(createUserSkillsDTOLazy(userSkill)));
        return userSkillsDTOS;
    }
}
