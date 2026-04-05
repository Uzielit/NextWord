package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.AuthResponseDto;
import com.nextword.backend.feature.user.dto.LoginRequestDto;
import com.nextword.backend.feature.user.dto.request.*;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.feature.user.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Este correo ya se encuentra registrado."));
        }

        String generatedId = authService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Estudiante Registrado con id " + generatedId));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Este correo ya está registrado en el sistema."));
        }

        String generatedId = authService.registerTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Profesor Registrado con id " + generatedId));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        try {
            String responseMessage = authService.forgotPassword(request);
            return ResponseEntity.ok(Map.of("message", responseMessage));
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        try {
            String responseMessage = authService.resetPassword(request);
            return ResponseEntity.ok(Map.of("message", responseMessage));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationMailRequestDto request) {
        try {
            String responseMessage = authService.verifyAccount(request);
            return ResponseEntity.ok(Map.of("message", responseMessage));
        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
