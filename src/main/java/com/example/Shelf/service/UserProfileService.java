package com.example.Shelf.service;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.Shelf.model.User;
import com.example.Shelf.model.UserProfile;
import com.example.Shelf.repository.UserProfileRepository;
import com.example.Shelf.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


@Service
public class UserProfileService {


    @Autowired
    private AmazonS3 amazonS3Client;


    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String S3_BUCKET_NAME = "shelfbackend";

    public void uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        String key = userId + "/" + file.getOriginalFilename();
        System.out.println(userId);
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new EntityNotFoundException("User not found");
        }

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(S3_BUCKET_NAME, key, file.getInputStream(), metadata);

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElse(null);

        if(userProfile == null){
          userProfile = new UserProfile();
          userProfile.setUser(user);
        }

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







