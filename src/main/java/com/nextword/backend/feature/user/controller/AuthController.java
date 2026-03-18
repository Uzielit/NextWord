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
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Este correo ya está registrado en el sistema.");
        }

        String generatedId = authService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante Registrado con id " + generatedId );
    }


    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(
            @Valid @RequestBody TeacherRegistrationRequest request){

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Este correo ya está registrado en el sistema.");
        }

        String generatedId = authService.registerTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profesor Registrado con id " + generatedId );
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        String responseMessage = authService.forgotPassword(request);
        return ResponseEntity.ok(responseMessage);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto request) {
        String responseMessage = authService.resetPassword(request);
        return ResponseEntity.ok(responseMessage);
    }


}
