package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.TeacherResponseDto;
import com.nextword.backend.feature.user.dto.update.TeacherProfileUpdateDto;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.feature.user.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherProfileController {
    private final AuthService authService;
    private final TeacherProfileRepository teacherProfileRepository;
    private final UserRepository userRepository;

    public TeacherProfileController(AuthService authService,
                                    TeacherProfileRepository teacherProfileRepository,
                                    UserRepository userRepository) {
        this.authService = authService;
        this.teacherProfileRepository = teacherProfileRepository;
        this.userRepository = userRepository;
    }
    @PutMapping("/profile")
    public ResponseEntity<String> updateTeacherProfile(
            Principal principal,
            @Valid @RequestBody TeacherProfileUpdateDto request) {
        String message = authService.completeTeacherProfile(principal.getName(), request);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TeacherProfile> getTeacherById(@PathVariable String id) {
        TeacherProfile profile = teacherProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me")
    public ResponseEntity<TeacherProfile> getMyProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TeacherProfile profile = teacherProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));

        return ResponseEntity.ok(profile);
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDto>> getAllTeachers() {
        List<TeacherResponseDto> teachers = teacherProfileRepository.findAll()
                .stream()
                .filter(t -> "ACTIVE".equals(t.getAccountStatus()))
                .map(t -> {
                    // Buscamos el nombre en la tabla User usando el mismo ID
                    User user = userRepository.findById(t.getId())
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    return new TeacherResponseDto(
                            t.getId(),
                            user.getFullName(),
                            t.getSpecialization(),
                            t.getAverageRating() != null ? t.getAverageRating() : 5.0 ,
                            t.getCertifications(),
                            t.getProfessionalDescription(),
                            t.getYearsOfExperience()
                    );
                })
                .toList();
        return ResponseEntity.ok(teachers);
    }

}
