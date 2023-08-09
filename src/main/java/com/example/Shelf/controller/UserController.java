package com.example.Shelf.controller;
import com.example.Shelf.model.User;
import com.example.Shelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (!userService.isUsernameAvailable(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        User createdUser = userService.createUser(user);
        return ResponseEntity.ok("User created successfully. ID: " + createdUser.getId());
    }

    @GetMapping("/fetchall")
    public ResponseEntity<List<User>> fetchall(){
        return ResponseEntity.ok(userService.fetchall());
    }
}