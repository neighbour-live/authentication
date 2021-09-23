package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Skill;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserSkill;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.SkillMapper;
import com.bot.middleware.persistence.mapper.UserSkillsMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddSkill;
import com.bot.middleware.persistence.request.AddUserSkill;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.SkillsService;
import com.bot.middleware.security.UserPrincipal;
import com.bot.middleware.utility.AuthConstants;
import com.bot.middleware.utility.StatusCode;
import com.bot.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillsService skillsService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add skill.")
    public ResponseEntity<?> addSkill(@Valid @RequestBody AddSkill addSkill) throws Exception {
        try {
            isCurrentUser(addSkill.getUserPublicId());
            Skill skill = skillsService.createSkill(addSkill);
            return GenericResponseEntity.create(StatusCode.SUCCESS, SkillMapper.createSkillDTOLazy(skill), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all skills.")
    public ResponseEntity<?> getAllSkills() throws Exception {
        try {
            List<Skill> skills = skillsService.getAllSkills();
            return GenericResponseEntity.create(StatusCode.SUCCESS, SkillMapper.createSkillDTOListLazy(skills), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/user/skill")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to create user skill.")
    public ResponseEntity<?> addUserSkill(@Valid @RequestBody AddUserSkill addUserSkill) throws Exception {
        try {
            User user = isCurrentUser(addUserSkill.getUserPublicId());
            UserSkill userSkill = skillsService.createUserSkill(addUserSkill, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserSkillsMapper.createUserSkillsDTOLazy(userSkill), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/user/skill/{userSkillPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit user skill.")
    public ResponseEntity<?> addUserSkill(@Valid @RequestBody AddUserSkill addUserSkill, @PathVariable String userSkillPublicId) throws Exception {
        try {
            User user = isCurrentUser(addUserSkill.getUserPublicId());
            skillsService.editUserSkill(addUserSkill, userSkillPublicId, user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Skill" + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/user/skill")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all skills of user.")
    public ResponseEntity<?> getAllUserSkills(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            List<UserSkill> userSkills = skillsService.getAllUserSkill(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserSkillsMapper.createUserSkillsDTOListLazy(userSkills), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{userSkillPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user skill.")
    public ResponseEntity<?> deleteUserSkill(@PathVariable String userSkillPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            skillsService.deleteUserSkill(userSkillPublicId, userPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Skill" + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    public User isCurrentUser(String userPublicId) throws ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        return user;
    }
}
