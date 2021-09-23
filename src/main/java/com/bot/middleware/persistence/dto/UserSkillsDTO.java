package com.bot.middleware.persistence.dto;

import com.bot.middleware.persistence.domain.Skill;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.type.SkillProficiency;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSkillsDTO {
    private String publicId;
    private String title;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private String skillProficiency;
    private SkillDTO skill;
    private String userPublicId;
}
