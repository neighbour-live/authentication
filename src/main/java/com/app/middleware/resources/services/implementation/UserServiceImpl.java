package com.app.middleware.resources.services.implementation;

import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserUpload;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.type.ServiceType;
import com.app.middleware.resources.services.S3BucketStorageService;
import com.app.middleware.resources.services.UserService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3BucketStorageService s3BucketStorageService;

    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    public User findByPublicId(Long decodePublicId) {
        return userRepository.findByPublicId(decodePublicId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User uploadIdentificationDocuments(User user, MultipartFile front, MultipartFile back) throws Exception {
        String frontDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-front";
        String backDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-back";
        if(front.getContentType().equals(MediaType.APPLICATION_PDF_VALUE) && back.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)){
            frontDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-front.pdf";
            backDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-back.pdf";
        } else if(front.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) && back.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)){
            frontDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-front.jpeg";
            backDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-back.jpeg";
        } else if(front.getContentType().equals(MediaType.IMAGE_PNG_VALUE) && back.getContentType().equals(MediaType.IMAGE_PNG_VALUE)){
            frontDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-front.png";
            backDocKeyName = PublicIdGenerator.encodedPublicId(user.getPublicId()) + "/" +"identification-back.png";
        } else {
            throw new Exception("The files must be in same format, allowed formats are "+ MediaType.APPLICATION_PDF_VALUE +", "+ MediaType.IMAGE_PNG_VALUE + " and "+ MediaType.IMAGE_JPEG_VALUE);
        }

        UserUpload frontDoc =  s3BucketStorageService.uploadFile(frontDocKeyName, ServiceType.Identification.name(), front, user, false);
        UserUpload backDoc =  s3BucketStorageService.uploadFile(backDocKeyName, ServiceType.Identification.name(), back, user, false);

        user.setIdDocFrontUrl(frontDoc.getKeyName());
        user.setIdDocBackUrl(backDoc.getKeyName());
        user.setIdentificationVerified(false);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
