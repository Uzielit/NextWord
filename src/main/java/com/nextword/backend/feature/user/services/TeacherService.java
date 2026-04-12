package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.user.dto.response.TeacherResponseDto;
import com.nextword.backend.feature.user.dto.update.TeacherProfileUpdateDto;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService {
    private final TeacherProfileRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherService(TeacherProfileRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, String> completeTeacherProfile(String email, TeacherProfileUpdateDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TeacherProfile profile = teacherRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));

        profile.setSpecialization(request.specialization());
        profile.setYearsOfExperience(request.yearsOfExperience());
        profile.setProfessionalDescription(request.professionalDescription());
        profile.setCertifications(request.certifications());
        profile.setAccountStatus("ACTIVE");
        teacherRepository.save(profile);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Perfil profesional actualizado exitosamente.");
        return response;
    }

    public TeacherResponseDto getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TeacherProfile profile = teacherRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));

        return new TeacherResponseDto(
                profile.getId(),
                user.getFullName(),
                profile.getSpecialization(),
                profile.getAverageRating() != null ? profile.getAverageRating() : 0.0,
                profile.getProfessionalDescription(),
                profile.getCertifications(),
                profile.getYearsOfExperience()
        );
    }

    public TeacherResponseDto getTeacherById(String id) {
        TeacherProfile profile = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new TeacherResponseDto(
                profile.getId(),
                user.getFullName(),
                profile.getSpecialization(),
                profile.getAverageRating() != null ? profile.getAverageRating() : 0.0,
                profile.getProfessionalDescription(),
                profile.getCertifications(),
                profile.getYearsOfExperience()
        );
    }

    public List<TeacherResponseDto> getAllActiveTeachers() {
        return teacherRepository.findAll()
                .stream()
                .filter(t -> "ACTIVE".equals(t.getAccountStatus()))
                .map(t -> {
                    User user = userRepository.findById(t.getId())
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    return new TeacherResponseDto(
                            t.getId(),
                            user.getFullName(),
                            t.getSpecialization(),
                            t.getAverageRating() != null ? t.getAverageRating() : 0.0,
                            t.getProfessionalDescription(),
                            t.getCertifications(),
                            t.getYearsOfExperience()
                    );
                })
                .toList();
    }
}
