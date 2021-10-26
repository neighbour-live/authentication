package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.UserAward;
import com.app.middleware.persistence.domain.UserUpload;
import com.app.middleware.persistence.repository.UserUploadRepository;
import com.app.middleware.resources.services.UserUploadsService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserUploadsServiceImpl implements UserUploadsService {

    @Autowired
    UserUploadRepository userUploadRepository;

    @Override
    public List<UserUpload> getAllUserUploads(Long publicId) {
        return userUploadRepository.findAllByPublicId(publicId);
    }

    @Override
    public UserUpload getUserUpload(Long publicId) throws ResourceNotFoundException {
        UserUpload userUpload = userUploadRepository.findByPublicId(publicId);
        if(userUpload == null){
            throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOT_FOUND_WITH, PublicIdGenerator.encodedPublicId(publicId));
        }
        return userUpload;
    }

    @Override
    public boolean deleteUserUpload(Long userPublicId, Long filePublicId) throws ResourceNotFoundException, UnauthorizedException {
        UserUpload userUpload = userUploadRepository.findByPublicId(filePublicId);
        if(userUpload == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.NOT_FOUND_WITH, PublicIdGenerator.encodedPublicId(filePublicId));
        if(!userUpload.getUser().getPublicId().equals(userPublicId)){
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        }
        userUpload.setIsDeleted(true);
        userUploadRepository.save(userUpload);
        return true;
    }
}
