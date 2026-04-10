package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.StudentProfileResponseDto;
import com.nextword.backend.feature.user.dto.update.StudentUpdateDto;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.feature.user.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {

    private final StudentService studentService;

    public StudentProfileController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponseDto> getMyProfile(Principal principal) {
        try {

            return ResponseEntity.ok(studentService.getMyProfile(principal.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            Principal principal,
            @RequestBody StudentUpdateDto updateDto) {
        try {
            studentService.updateStudentProfile(principal.getName(), updateDto);


            Map<String, String> response = new HashMap<>();
            response.put("message", "Perfil actualizado correctamente");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
