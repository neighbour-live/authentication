package com.app.middleware.resources.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3BucketStorageService {

    /**
     * Upload file into AWS S3
     *
     * @param keyName
     * @param file
     * @return String
     */
    String uploadFile(String keyName, MultipartFile file);

    List<String> listFiles(String keyName);
}
