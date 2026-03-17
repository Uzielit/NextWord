package com.nextword.backend.feature.user.controller;


import com.nextword.backend.feature.user.dto.update.UserUpdateDto;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          StudentProfileRepository studentProfileRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
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
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            Principal principal,
            @RequestBody UserUpdateDto updateDto) {

        String emailToken = principal.getName();

        return userRepository.findByEmail(emailToken)
                .map(user -> {
                    if (updateDto.fullName() != null) user.setFullName(updateDto.fullName());
                    if (updateDto.phoneNumber() != null) user.setPhoneNumber(updateDto.phoneNumber());
                    if (updateDto.profilePicture() != null) user.setProfilePicture(updateDto.profilePicture());

                    if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(updateDto.newPassword()));
                    }
                    userRepository.save(user);


                    return ResponseEntity.ok("Perfil actualizado correctamente");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
