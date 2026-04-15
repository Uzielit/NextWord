package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.user.dto.response.StudentProfileResponseDto;
import com.nextword.backend.feature.user.dto.update.StudentUpdateDto;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(UserRepository userRepository,
                          StudentProfileRepository studentProfileRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // MÉTODO PARA VER EL PERFIL
    @Transactional(readOnly = true)
    public StudentProfileResponseDto getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        StudentProfile profile = studentProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        return new StudentProfileResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getWalletBalance(),
                profile.getDateOfBirth(),
                profile.getTutorName(),
                profile.getTutorEmail(),
                profile.getTutorPhone()
        );
    }

    // MÉTODO PARA ACTUALIZAR
    @Transactional
    public void updateStudentProfile(String email, StudentUpdateDto updateDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos tabla base
        if (updateDto.fullName() != null) user.setFullName(updateDto.fullName());
        if (updateDto.phoneNumber() != null) user.setPhoneNumber(updateDto.phoneNumber());
        if (updateDto.profilePicture() != null) user.setProfilePicture(updateDto.profilePicture());
        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateDto.newPassword()));
        }
        userRepository.save(user);


        StudentProfile profile = studentProfileRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

        if (updateDto.tutorName() != null) profile.setTutorName(updateDto.tutorName());
        if (updateDto.tutorPhone() != null) profile.setTutorPhone(updateDto.tutorPhone());
        if (updateDto.tutorEmail() != null) profile.setTutorEmail(updateDto.tutorEmail());

        studentProfileRepository.save(profile);
    }
}
