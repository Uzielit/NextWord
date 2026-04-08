package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.StudentProfileResponseDto;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {
    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileController(StudentProfileRepository studentProfileRepository
    , UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileResponseDto> getMyProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        StudentProfile profile = studentProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

        StudentProfileResponseDto response = new StudentProfileResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getWalletBalance(),
                profile.getDateOfBirth(),
                profile.getTutorName(),
                profile.getTutorEmail(),
                profile.getTutorPhone()
        );
        return ResponseEntity.ok(response);
    }


}
