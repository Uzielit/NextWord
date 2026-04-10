package com.nextword.backend.feature.user.controller;


import com.nextword.backend.feature.user.dto.update.StudentUpdateDto;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.feature.user.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            Principal principal,
            @RequestBody StudentUpdateDto updateDto) {
        try {
            userService.updateStudentProfile(principal.getName(), updateDto);
            return ResponseEntity.ok("Perfil actualizado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        try {

            Map<String, Object> userData = userService.getCurrentUserData(principal.getName());
            return ResponseEntity.ok(userData);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
