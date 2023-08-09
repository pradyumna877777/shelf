package com.example.Shelf.service;
import com.example.Shelf.model.User;
import com.example.Shelf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // Additional validation and business logic can be added here
        return userRepository.save(user);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    public List<User> fetchall(){
        return userRepository.findAll();
    }
}