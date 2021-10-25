package com.app.middleware.resources.services.implementation;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserUpload;
import com.app.middleware.persistence.repository.UserUploadRepository;
import com.app.middleware.persistence.type.ServiceType;
import com.app.middleware.resources.services.S3BucketStorageService;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Service
public class S3BucketStorageServiceImpl implements S3BucketStorageService {
    private Logger logger = LoggerFactory.getLogger(S3BucketStorageServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.bucket-public}")
    private String bucketNamePublic;

    @Autowired
    private UserUploadRepository userUploadRepository;

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserUpload uploadFile(String keyName, String serviceName, MultipartFile file, User user, boolean isPublic) throws Exception {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            if(ServiceType.Identification.equals(ServiceType.valueOf(serviceName)) || String.valueOf(ServiceType.valueOf(serviceName)).isEmpty())
            {
                throw new Exception("File cannot be uploaded for this service");
            }

            if(isPublic){
                amazonS3.putObject(bucketNamePublic, keyName, file.getInputStream(), metadata);
                amazonS3.setObjectAcl(bucketNamePublic, keyName, CannedAccessControlList.PublicRead);
                URL url = amazonS3.getUrl(bucketNamePublic, keyName);

                UserUpload userUpload = new UserUpload();
                userUpload.setUser(user);
                userUpload.setPublicId(PublicIdGenerator.generatePublicId());
                userUpload.setServiceType(serviceName);
                userUpload.setIsDeleted(false);
                userUpload.setKeyName(keyName);
                userUpload.setPublicUrl(url.toString());
                userUpload = userUploadRepository.save(userUpload);
                return userUpload;
            }

            amazonS3.putObject(bucketName, keyName, file.getInputStream(), metadata);
            UserUpload userUpload = new UserUpload();
            userUpload.setUser(user);
            userUpload.setPublicId(PublicIdGenerator.generatePublicId());
            userUpload.setServiceType(serviceName);
            userUpload.setIsDeleted(false);
            userUpload.setKeyName(keyName);
            userUpload.setPublicUrl("");
            userUpload = userUploadRepository.save(userUpload);
            return userUpload;
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        throw new Exception("File not uploaded: " + keyName);
    }

    @Override
    public UserUpload getFile(String keyName) throws ResourceNotFoundException {
        UserUpload userUpload =  userUploadRepository.findByKeyName(keyName);
        if(userUpload == null) {
            throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_FILE_NOT_FOUND_WITH_KEY_NAME, keyName);
        }
        return userUpload;
//        try {
//            S3Object s3Object = amazonS3.getObject(bucketName,keyName);
//            urls.add(s3Object.getObjectContent().toString());
//            return  urls;
//        } catch (AmazonServiceException serviceException) {
//            logger.info("AmazonServiceException: "+ serviceException.getMessage());
//            throw serviceException;
//        } catch (AmazonClientException clientException) {
//            logger.info("AmazonClientException Message: " + clientException.getMessage());
//            throw clientException;
//        }
//        return null;
    }

    @Override
    public byte[] getFileByKeyName(String keyName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName, keyName);
//        OutputStream out = new FileOutputStream("out.pdf");
//        out.write(s3Object.getObjectContent().readAllBytes());
//        out.close();
        return s3Object.getObjectContent().readAllBytes();
    }
}
