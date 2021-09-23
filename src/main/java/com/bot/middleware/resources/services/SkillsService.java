package com.bot.middleware.resources.services;

import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Skill;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserSkill;
import com.bot.middleware.persistence.request.AddSkill;
import com.bot.middleware.persistence.request.AddUserSkill;

import java.util.List;

public interface SkillsService {
    Skill createSkill(AddSkill addSkill);

    List<Skill> getAllSkills();

    boolean deleteSkill(String skillPublicId) throws ResourceNotFoundException;

    UserSkill createUserSkill(AddUserSkill addUserSkill, User user) throws ResourceNotFoundException;

    List<UserSkill> getAllUserSkill(String userPublicId);

    boolean deleteUserSkill(String skillPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException;

    UserSkill editUserSkill(AddUserSkill addUserSkill, String userSkillPublicId, User user) throws ResourceNotFoundException, UnauthorizedException;
}
