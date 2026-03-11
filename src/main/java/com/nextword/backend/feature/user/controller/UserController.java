package com.nextword.backend.feature.user.controller;


import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

private final UserRepository userRepository;
public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
    //EndPoint GET
    @GetMapping
    public List<User> getAllUsers() {
    return userRepository.findAll();
    }
    //EndPoint Post
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }


}
