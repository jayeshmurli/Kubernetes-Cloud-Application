package com.neu.backend.services;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

@Service
public class AmazonS3FileUploadService {

    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private TransferManager tm;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3FileUploadService.class);

    @Autowired
    public AmazonS3FileUploadService(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket)
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName())
                .build();

        this.tm = TransferManagerBuilder.standard()
                .withS3Client(this.amazonS3)
                .build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }

    public String uploadFile(MultipartFile multipartFile) throws Exception {

        String fileName = generateFileName(multipartFile);

        try {

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, fileName, multipartFile.getInputStream(), new ObjectMetadata());
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);

            Upload upload = tm.upload(putObjectRequest);
            upload.waitForCompletion();
            logger.info("Object URL: "+ amazonS3.getUrl(this.awsS3AudioBucket, fileName).toString());

            return amazonS3.getUrl(this.awsS3AudioBucket, fileName).toString();


        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
            throw ex;
        }


    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }


    public void deleteFile(String fileName) throws AmazonServiceException{

        amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));


    }


}
