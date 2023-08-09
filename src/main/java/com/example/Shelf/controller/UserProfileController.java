package com.example.Shelf.controller;

import com.example.Shelf.service.UserProfileService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile/users/{userId}")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PutMapping
    public ResponseEntity<String> uploadProfileImage(@RequestPart("file") MultipartFile file,
                                                     @PathVariable("userId") Long userId) {
        try {
            System.out.println("check4");
            System.out.println(userId);

            userProfileService.uploadProfileImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProfileImage(@PathVariable("userId") Long userId) {
        try {
            userProfileService.deleteProfileImage(userId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image delete failed");
        }
    }

    @GetMapping
    public ResponseEntity<String> fetchProfileImageUrl(@PathVariable("userId") Long userId) {
        try {
            String imageUrl = userProfileService.getProfileImageUrl(userId);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile image not found");
        }
    }
}
