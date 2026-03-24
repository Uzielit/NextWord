package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.update.TeacherProfileUpdateDto;
import com.nextword.backend.feature.user.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherProfileController {
    private final AuthService authService;

    public TeacherProfileController(AuthService authService) {
        this.authService = authService;
    }
    @PutMapping("/profile")
    public ResponseEntity<String> updateTeacherProfile(
            Principal principal,
            @Valid @RequestBody TeacherProfileUpdateDto request) {
        String message = authService.completeTeacherProfile(principal.getName(), request);
        return ResponseEntity.ok(message);
    }
}
