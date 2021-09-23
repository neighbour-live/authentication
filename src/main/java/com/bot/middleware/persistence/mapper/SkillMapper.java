package com.bot.middleware.persistence.mapper;

import com.bot.middleware.persistence.domain.Skill;
import com.bot.middleware.persistence.dto.SkillDTO;
import com.bot.middleware.utility.id.PublicIdGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SkillMapper {

    public static SkillDTO createSkillDTOLazy(Skill skill) {
        SkillDTO skillDTO = SkillDTO.builder()
                .publicId(PublicIdGenerator.encodedPublicId(skill.getPublicId()))
                .name(skill.getName())
                .skillIcon(skill.getSkillIcon())
                .build();

        return skillDTO;
    }

    public static List<SkillDTO> createSkillDTOListLazy(Collection<Skill> skills) {
        List<SkillDTO> skillDTOS = new ArrayList<>();
        skills.forEach(skill -> skillDTOS.add(createSkillDTOLazy(skill)));
        return skillDTOS;
    }
}
