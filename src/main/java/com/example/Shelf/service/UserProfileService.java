package com.example.Shelf.service;
import com.example.Shelf.model.UserProfile;
import com.example.Shelf.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserProfileService {

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .build();
    @Autowired
    private AmazonS3 amazonS3Client;


    @Autowired
    private UserProfileRepository userProfileRepository;

    private static final String S3_BUCKET_NAME = "shelfbackend";

    public void uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        String key = userId + "/" + file.getOriginalFilename();
        amazonS3Client.putObject(S3_BUCKET_NAME, key, file.getInputStream(), null);

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElse(new UserProfile(userId, ""));
        userProfile.setImageUrl(getS3ImageUrl(key));
        userProfileRepository.save(userProfile);
    }

    public void deleteProfileImage(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));

        if (!userProfile.getImageUrl().isEmpty()) {
            String key = userProfile.getImageUrl().substring(userProfile.getImageUrl().lastIndexOf('/') + 1);
            amazonS3Client.deleteObject(S3_BUCKET_NAME, key);
        }

        userProfile.setImageUrl("");
        userProfileRepository.save(userProfile);
    }

    public void updateProfileImage(Long userId, MultipartFile file) throws IOException {
        deleteProfileImage(userId); // Delete existing image

        uploadProfileImage(userId, file); // Upload new image
    }

    public String getProfileImageUrl(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found"));

        return userProfile.getImageUrl();
    }

    public String getS3ImageUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // URL expiration time (1 hour)
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET_NAME, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}







