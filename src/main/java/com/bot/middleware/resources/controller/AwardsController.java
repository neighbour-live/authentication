package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.Award;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAward;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.AwardMapper;
import com.bot.middleware.persistence.mapper.UserAwardsMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddUserAward;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.AwardsService;
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
@RequestMapping("/awards")
public class AwardsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwardsService awardsService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all the awards.")
    public ResponseEntity<?> getAllAward() throws Exception {
        try {
            List<Award> awards = awardsService.getAllAwards();
            return GenericResponseEntity.create(StatusCode.SUCCESS, AwardMapper.createAwardDTOListLazy(awards), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/user-award")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used add user award.")
    public ResponseEntity<?> addUserAward(@Valid @RequestBody AddUserAward addUserAward) throws Exception {
        try {
            User user = isCurrentUser(addUserAward.getUserPublicId());
            Award award = awardsService.getByAwardId(addUserAward.getAwardPublicId());
            UserAward userAward = awardsService.addUserAward(addUserAward, user, award);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserAwardsMapper.createUserAwardsDTOLazy(userAward), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/user-award/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used fetch all user award.")
    public ResponseEntity<?> getAllAward(@PathVariable String userPublicId) throws Exception {
        try {
            User user = isCurrentUser(userPublicId);
            List<UserAward> userAwards = awardsService.getAllUserAwards(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserAwardsMapper.createUserAwardsDTOListLazy(userAwards), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{userPublicId}/user-award/{userAwardPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch user award detail.")
    public ResponseEntity<?> getAllAward(@PathVariable String userPublicId, @PathVariable String userAwardPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserAward userAward = awardsService.getUserAward(userAwardPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserAwardsMapper.createUserAwardsDTOLazy(userAward), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{userPublicId}/user-award/{userAwardPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user award.")
    public ResponseEntity<?> deleteUserAward(@PathVariable String userPublicId, @PathVariable String userAwardPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            awardsService.deleteUserAward(userAwardPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Award " + AuthConstants.DELETED_SUCCESSFULLY)
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
