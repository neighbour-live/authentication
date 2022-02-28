package com.app.middleware.resources.services;

import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3BucketStorageService {

    /**
     * Upload file into AWS S3
     *
     *
     * @param keyName
     * @param serviceName
     * @param file
     * @param user
     * @return UserUpload
     */
    UserUpload uploadFile(String keyName, String serviceName, MultipartFile file, User user, boolean isPublic) throws Exception;

    UserUpload getFile(String keyName) throws ResourceNotFoundException;

    byte[] getFileByKeyName(String keyName) throws IOException;
}
