package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.TeacherResponseDto;
import com.nextword.backend.feature.user.dto.update.TeacherProfileUpdateDto;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherProfileController {
    private final TeacherService teacherService;

    public TeacherProfileController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateTeacherProfile(
            Principal principal,
            @Valid @RequestBody TeacherProfileUpdateDto request) {
        try {
            Map<String, String> response = teacherService.completeTeacherProfile(principal.getName(), request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<TeacherResponseDto> getMyProfile(Principal principal) {
        try {
            return ResponseEntity.ok(teacherService.getMyProfile(principal.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherProfile> getTeacherById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(teacherService.getTeacherById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllActiveTeachers());
    }


}
