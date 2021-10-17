package com.app.middleware.resources.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3BucketStorageService {

    /**
     * Upload file into AWS S3
     *
     * @param keyName
     * @param file
     * @return String
     */
    String uploadFile(String keyName, MultipartFile file, boolean isPublic);

    List<String> listFiles(String keyName);

    byte[] getFileByKeyName(String keyName) throws IOException;
}
