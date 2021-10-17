package com.app.middleware.resources.services.implementation;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.app.middleware.resources.services.LoggingService;
import com.app.middleware.resources.services.S3BucketStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3BucketStorageServiceImpl implements S3BucketStorageService {
    private Logger logger = LoggerFactory.getLogger(S3BucketStorageServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.bucket-public}")
    private String bucketNamePublic;

    /**
     * Upload file into AWS S3
     *
     * @param keyName
     * @param file
     * @return String
     */
    public String uploadFile(String keyName, MultipartFile file, boolean isPublic) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            if(isPublic){
                amazonS3.putObject(bucketNamePublic, keyName, file.getInputStream(), metadata);
                amazonS3.setObjectAcl(bucketNamePublic, keyName, CannedAccessControlList.PublicRead);
                URL url = amazonS3.getUrl(bucketNamePublic, keyName);
                return url.toString();
            }

            amazonS3.putObject(bucketName, keyName, file.getInputStream(), metadata);
            return keyName;
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        return "File not uploaded: " + keyName;
    }

    @Override
    public List<String> listFiles(String keyName) {
        List<String> urls = new ArrayList<>();
        try {
            S3Object s3Object = amazonS3.getObject(bucketName,keyName);
            urls.add(s3Object.getObjectContent().toString());
            return  urls;
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
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
