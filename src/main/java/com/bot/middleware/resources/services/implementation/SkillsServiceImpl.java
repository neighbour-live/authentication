package com.bot.middleware.resources.services.implementation;

import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Skill;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserSkill;
import com.bot.middleware.persistence.repository.SkillRepository;
import com.bot.middleware.persistence.repository.UserSkillsRepository;
import com.bot.middleware.persistence.request.AddSkill;
import com.bot.middleware.persistence.request.AddUserSkill;
import com.bot.middleware.persistence.type.SkillProficiency;
import com.bot.middleware.resources.services.SkillsService;
import com.bot.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillsServiceImpl implements SkillsService {

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    UserSkillsRepository userSkillsRepository;

    @Override
    public Skill createSkill(AddSkill addSkill) {

        Skill skill = new Skill();
        skill.setName(addSkill.getName());
        skill.setPublicId(PublicIdGenerator.generatePublicId());
        skill.setSkillIcon(addSkill.getSkillIconUrl());
        skill.setIsApproved(true);
        return skillRepository.save(skill);

    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public boolean deleteSkill(String skillPublicId) throws ResourceNotFoundException {
        Skill skill = skillRepository.findByPublicId(PublicIdGenerator.decodePublicId(skillPublicId));
        if(skill == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.SKILL_NOT_FOUND_WITH_PUBLIC_ID, skillPublicId);

        skillRepository.delete(skill);
        return true;
    }

    @Override
    public UserSkill createUserSkill(AddUserSkill addUserSkill, User user) throws ResourceNotFoundException {
        Skill skill = skillRepository.findByPublicId(PublicIdGenerator.decodePublicId(addUserSkill.getSkillPublicId()));
        if(skill == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.SKILL_NOT_FOUND_WITH_PUBLIC_ID, addUserSkill.getSkillPublicId());

        UserSkill userSkill = new UserSkill();
        userSkill.setPublicId(PublicIdGenerator.generatePublicId());
        userSkill.setSkill(skill);
        userSkill.setUser(user);
        userSkill.setSkillProficiency(SkillProficiency.valueOf(addUserSkill.getSkillProficiency()));
        userSkill.setIsActive(true);
        userSkill.setIsDeleted(false);
        userSkill.setTitle(addUserSkill.getTitle());
        userSkill.setDescription(addUserSkill.getDescription());

        return userSkillsRepository.save(userSkill);
    }

    @Override
    public List<UserSkill> getAllUserSkill(String userPublicId) {
        return userSkillsRepository.findAllByUserAndIsDeletedIsFalse(PublicIdGenerator.decodePublicId(userPublicId));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteUserSkill(String userSkillPublicId, String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        UserSkill userSkill = userSkillsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userSkillPublicId));
        if(userSkill == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_SKILL_NOT_FOUND_WITH_PUBLIC_ID, userSkillPublicId);
        if(!userSkill.getUser().getPublicId().equals(PublicIdGenerator.decodePublicId(userPublicId))) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        userSkillsRepository.deleteUserSkill(userSkill.getPublicId());
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserSkill editUserSkill(AddUserSkill addUserSkill, String userSkillPublicId, User user) throws ResourceNotFoundException, UnauthorizedException {
        UserSkill userSkill = userSkillsRepository.findByPublicId(PublicIdGenerator.decodePublicId(userSkillPublicId));
        if(userSkill == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_SKILL_NOT_FOUND_WITH_PUBLIC_ID, addUserSkill.getSkillPublicId());
        if(!userSkill.getUser().getPublicId().equals(user.getPublicId())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        Skill skill = skillRepository.findByPublicId(PublicIdGenerator.decodePublicId(addUserSkill.getSkillPublicId()));
        if(skill == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.SKILL_NOT_FOUND_WITH_PUBLIC_ID, addUserSkill.getSkillPublicId());

        userSkill.setSkill(skill);
        userSkill.setUser(user);
        userSkill.setSkillProficiency(SkillProficiency.valueOf(addUserSkill.getSkillProficiency()));
        userSkill.setIsActive(true);
        userSkill.setIsDeleted(false);
        userSkill.setTitle(addUserSkill.getTitle());
        userSkill.setDescription(addUserSkill.getDescription());
        return userSkillsRepository.save(userSkill);
    }
}
