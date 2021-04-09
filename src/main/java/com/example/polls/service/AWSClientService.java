package com.example.polls.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AWSClientService {

    private AmazonS3 amazonS3;

    @Value("${amazon.s3.endpoint}")
    private String endPoint;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Value("${amazon.s3.access-key}")
    private String accessKey;

    // The IAM secret key.
    @Value("${amazon.s3.secret-key}")
    private String secretKey;

    // Getters for parents.
    protected AmazonS3 getClient() {
        return amazonS3;
    }

    protected String getEndPoint() {
        return endPoint;
    }

    protected String getBucketName() {
        return bucketName;
    }

    // This method are called after Spring starts AmazonClientService into your container.
    @PostConstruct
    private void init() {

        // Init your AmazonS3 credentials using BasicAWSCredentials.
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        // Start the client using AmazonS3ClientBuilder, here we goes to make a standard cliente, in the
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_NORTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
