package com.example.Shelf.controller;


import com.example.Shelf.model.UserProfile;
import com.example.Shelf.service.UserProfileService;
//import com.example.Shelf.dto.CreateCampaignRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("userId") Long userId) {
        try {
            userProfileService.uploadProfileImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfileImage(@RequestParam("userId") Long userId) {
        try {
            userProfileService.deleteProfileImage(userId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image delete failed");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfileImage(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("userId") Long userId) {
        try {
            userProfileService.updateProfileImage(userId, file);
            return ResponseEntity.ok("Image updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image update failed");
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchProfileImageUrl(@RequestParam("userId") Long userId) {
        try {
            String imageUrl = userProfileService.getProfileImageUrl(userId);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile image not found");
        }
    }
}
