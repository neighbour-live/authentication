package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserUpload;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.mapper.UserUploadMapper;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthorizationService;
import com.app.middleware.resources.services.UserUploadsService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import com.app.middleware.utility.id.PublicIdGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user-uploads")
public class UserUploadsController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserUploadsService userUploadsService;

    @GetMapping("/all/{userPublicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to fetch all the user uploads.")
    public ResponseEntity<?> getAllUserUploads(@PathVariable String userPublicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            List<UserUpload> userUploads = userUploadsService.getAllUserUploads(PublicIdGenerator.decodePublicId(userPublicId));
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserUploadMapper.createUserUploadDTOListLazy(userUploads), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/{userPublicId}/upload/{publicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used fetch one user upload")
    public ResponseEntity<?> getUserUpload(@PathVariable String userPublicId, @PathVariable String publicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            UserUpload userUpload = userUploadsService.getUserUpload(PublicIdGenerator.decodePublicId(publicId));
            if(!userUpload.getUser().getPublicId().equals(user.getPublicId())){
                throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
            }
            return GenericResponseEntity.create(StatusCode.SUCCESS, UserUploadMapper.createUserUploadDTOLazy(userUpload), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/{userPublicId}/upload/{publicId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "This operation is used to delete user upload.")
    public ResponseEntity<?> deleteUserUpload(@PathVariable String userPublicId, @PathVariable String publicId) throws Exception {
        try {
            User user = authorizationService.isCurrentUser(userPublicId);
            userUploadsService.deleteUserUpload(user.getPublicId(), PublicIdGenerator.decodePublicId(publicId));
            return GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message("User Upload " + AuthConstants.DELETED_SUCCESSFULLY)
                    .status(0)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }
}
