package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.UserUpload;

import java.util.List;

public interface UserUploadsService {
    List<UserUpload> getAllUserUploads(Long publicId);

    UserUpload getUserUpload(Long decodePublicId) throws ResourceNotFoundException;

    boolean deleteUserUpload(Long userPublicId, Long filePublicId) throws ResourceNotFoundException, UnauthorizedException;
}
