package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.request.RoleRequest;
import com.nextword.backend.feature.user.dto.request.StudentRegistrationRequest;
import com.nextword.backend.feature.user.dto.request.TeacherRegistrationRequest;
import com.nextword.backend.feature.user.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@RequestBody StudentRegistrationRequest request) {
        String generatedId = authService.registerStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante Resgistrado con id " + generatedId );
    }


    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(@RequestBody TeacherRegistrationRequest request){
        String generatedId = authService.registerTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Profesor Resgistrado con id " + generatedId );
    }



}
