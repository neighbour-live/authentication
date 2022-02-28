package com.app.middleware.resources.controller;

import com.app.middleware.exception.ResourceNotFoundException;
import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserMapper;
import com.app.middleware.persistence.request.EditProfileRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthService;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.S3BucketStorageService;
import com.app.middleware.resources.services.UserService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3BucketStorageService s3BucketStorageService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch details of logged in user.")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) throws Exception {
        try {
            Optional<User> user = Optional.ofNullable(userService.findById(userPrincipal.getId())
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
            User user = authorizationService.isCurrentUser(editProfileRequest.getUserPublicId());
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

    @PostMapping("/upload-identification")
    @ApiOperation(value = "This operation is used to upload identification document of a user")
    public ResponseEntity<?> confirmPhonePreRegister(@RequestParam("userPublicId") String userPublicId,
                                                     @RequestParam("front") MultipartFile front,
                                                     @RequestParam("back") MultipartFile back) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            user = userService.uploadIdentificationDocuments(user, front, back);
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserMapper.createUserIdentificationDTOLazy(user), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to logged out user from application.")
    public ResponseEntity<?> logout(@RequestParam("publicId") String publicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(publicId);
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
            authorizationService.isCurrentUser(publicId);
            HashMap<Object, Boolean> response = authService.getUserVerificationStatus(publicId);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/file")
    @ApiOperation(value = "This operation is used to get bytes of file by its key name")
    public ResponseEntity<?> getFileByKeyName(@RequestParam("keyName") String keyName, @RequestParam("userPublicId") String userPublicId) throws UnauthorizedException, com.app.middleware.exceptions.type.ResourceNotFoundException, IOException {
        User user = authorizationService.isCurrentUser(userPublicId);
        return new ResponseEntity<>(s3BucketStorageService.getFileByKeyName(keyName), HttpStatus.OK);
    }

    @GetMapping("/profile-completion")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to calculate profile completion percentage.")
    public ResponseEntity<?> profileCompletion(@RequestParam("publicId") String publicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(publicId);
            int response = authService.calculateProfileCompletionPercentage(user);
            return GenericResponseEntity.create(StatusCode.SUCCESS, response, HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
