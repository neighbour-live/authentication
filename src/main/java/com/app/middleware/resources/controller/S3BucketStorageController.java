package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.resources.services.S3BucketStorageService;
import com.app.middleware.resources.services.UserService;
import com.app.middleware.security.UserPrincipal;
import com.app.middleware.utility.Utility;
import com.app.middleware.utility.id.PublicIdGenerator;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/bucket")
public class S3BucketStorageController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3BucketStorageService s3BucketStorageService;

    @GetMapping("/list/files")
    public ResponseEntity<List<String>> getListOfFiles(@RequestParam("fileName") String fileName, @RequestParam("userPublicId") String userPublicId) throws UnauthorizedException, ResourceNotFoundException {
        User user = isCurrentUser(userPublicId);
        return new ResponseEntity<>(s3BucketStorageService.listFiles(fileName), HttpStatus.OK);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName") String fileName,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("userPublicId") String userPublicId) throws Exception {
        User user = isCurrentUser(userPublicId);
        String docKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" + Utility.generateOTP();
        if(file.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)){
            docKeyName = docKeyName+".pdf";
        } else if(file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)){
            docKeyName = docKeyName+".jpeg";
        } else if(file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)){
            docKeyName = docKeyName+".png";
        } else {
            throw new Exception("The files must be in same format, allowed formats are "+ MediaType.APPLICATION_PDF_VALUE +", "+ MediaType.IMAGE_PNG_VALUE + " and "+ MediaType.IMAGE_JPEG_VALUE);
        }
        return new ResponseEntity<>(s3BucketStorageService.uploadFile(docKeyName, file, true), HttpStatus.OK);
    }

    public User isCurrentUser(String userPublicId) throws com.app.middleware.exceptions.type.ResourceNotFoundException, UnauthorizedException {
        User user = userService.findByPublicId(PublicIdGenerator.decodePublicId(userPublicId));
        if(user == null) throw new com.app.middleware.exceptions.type.ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, userPublicId);

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userPrincipal.getEmail().equals(user.getEmail())) throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION);
        return user;
    }
}
