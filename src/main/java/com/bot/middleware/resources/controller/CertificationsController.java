package com.bot.middleware.resources.controller;

import com.bot.middleware.exceptions.ExceptionUtil;
import com.bot.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.bot.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.exceptions.type.UnauthorizedException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserCertification;
import com.bot.middleware.persistence.dto.StatusMessageDTO;
import com.bot.middleware.persistence.mapper.UserCertificationsMapper;
import com.bot.middleware.persistence.repository.UserRepository;
import com.bot.middleware.persistence.request.AddUserCertification;
import com.bot.middleware.persistence.response.GenericResponseEntity;
import com.bot.middleware.resources.services.CertificationsService;
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
@RequestMapping("/certifications")
public class CertificationsController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CertificationsService certificationsService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to add Certification.")
    public ResponseEntity<?> addCertification(@Valid @RequestBody AddUserCertification addUserCertification) throws Exception {
        try {
            User user = isCurrentUser(addUserCertification.getUserPublicId());
            UserCertification userCertification = certificationsService.createUserCertification(addUserCertification, user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserCertificationsMapper.createUserCertificationsDTOLazy(userCertification), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PutMapping("/{userCertificationPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit Certification.")
    public ResponseEntity<?> editCertification(@Valid @RequestBody AddUserCertification addUserCertification, @PathVariable String userCertificationPublicId) throws Exception {
        try {
            User user = isCurrentUser(addUserCertification.getUserPublicId());
            certificationsService.editUserCertification(addUserCertification, userCertificationPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Certification" + AuthConstants.EDITED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all the user certifications.")
    public ResponseEntity<?> getAllCertifications(@RequestParam("userPublicId") String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            List<UserCertification> userCertifications = certificationsService.getAllUserCertifications(userPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserCertificationsMapper.createUserCertificationsDTOListLazy(userCertifications), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{userCertificationPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch details of specific certification.")
    public ResponseEntity<?> getByUserCertificationId(@PathVariable String userCertificationPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            UserCertification userCertification = certificationsService.getByUserCertificationId(userCertificationPublicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserCertificationsMapper.createUserCertificationsDTOLazy(userCertification), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{userCertificationPublicId}/user/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user certification.")
    public ResponseEntity<?> deleteCertification(@PathVariable String userCertificationPublicId, @PathVariable String userPublicId) throws Exception {
        try {
            isCurrentUser(userPublicId);
            certificationsService.deleteUserCertification(userCertificationPublicId, userPublicId);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Certification" + AuthConstants.DELETED_SUCCESSFULLY)
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
