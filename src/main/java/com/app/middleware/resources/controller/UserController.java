package com.app.middleware.resources.controller;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserMapper;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.EditProfileRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthService;
import com.app.middleware.security.CurrentUser;
import com.app.middleware.security.UserPrincipal;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch details of logged in user.")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws Exception {
        try {
            Optional<User> user = Optional.ofNullable(userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId())));
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserMapper.createUserDTOLazy(user.get()), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }

    }

    @PutMapping("/edit-profile")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to edit user profile.")
    public ResponseEntity<?> editProfile(@Valid @RequestBody EditProfileRequest editProfileRequest) throws Exception {
        try {
            User user = isCurrentUser(editProfileRequest.getUserPublicId());
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message(authService.editProfile(editProfileRequest, user))
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/confirm-email")
    @ApiOperation(value = "This operation is used to confirm User Email.")
    public ResponseEntity<?> confirmEmailPreRegister(@RequestParam("email") String email, @RequestParam("emailCode") String emailCode) throws Exception {

        try {
            User user  = authService.confirmEmail(email, emailCode);
            return GenericResponseEntity.create(StatusCode.SUCCESS, AuthConstants.EDIT_PROFILE_SUCCESSFUL, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/confirm-phone")
    @ApiOperation(value = "This operation is used to confirm phone number.")
    public ResponseEntity<?> confirmPhonePreRegister(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("phoneCode") String phoneCode) throws Exception {
        try {
            User user = authService.confirmPhoneNumber(phoneNumber, phoneCode);
            return GenericResponseEntity.create(StatusCode.SUCCESS, AuthConstants.EDIT_PROFILE_SUCCESSFUL, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to logged out user from application.")
    public ResponseEntity<?> logout(@RequestParam("publicId") String publicId) throws Exception {
        try {
            User user = isCurrentUser(publicId);
            authService.logout(user);
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message(AuthConstants.LOGOUT_SUCCESS)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/verification-status")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch user verification status.")
    public ResponseEntity<?> verificationStatus(@RequestParam("publicId") String publicId) throws Exception {
        try {
            isCurrentUser(publicId);
            HashMap<Object, Boolean> response = authService.getUserVerificationStatus(publicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/profile-completion")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to calculate profile completion percentage.")
    public ResponseEntity<?> profileCompletion(@RequestParam("publicId") String publicId) throws Exception {
        try {
            User user = isCurrentUser(publicId);
            int response = authService.calculateProfileCompletionPercentage(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    public User isCurrentUser(String userPublicId) throws com.app.middleware.exceptions.type.ResourceNotFoundException, UnauthorizedException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new com.app.middleware.exceptions.type.ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);

        return user;
    }
}
