package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.user.dto.update.StudentUpdateDto;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void updateStudentProfile(String email, StudentUpdateDto updateDto) {
        // 1. Buscamos al usuario en la tabla principal
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        // 2. Actualizamos la tabla 'Usuario'
        if (updateDto.fullName() != null) user.setFullName(updateDto.fullName());
        if (updateDto.phoneNumber() != null) user.setPhoneNumber(updateDto.phoneNumber());
        if (updateDto.profilePicture() != null) user.setProfilePicture(updateDto.profilePicture());

        if (updateDto.newPassword() != null && !updateDto.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateDto.newPassword()));
        }
        userRepository.save(user);

        // 3. Verificamos si es Estudiante (id_rol = 1) para actualizar la tabla 'perfil_estudiante'
        if (user.getRoleId() == 1) {
            StudentProfile profile = studentProfileRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

            if (updateDto.tutorName() != null) profile.setTutorName(updateDto.tutorName());
            if (updateDto.tutorPhone() != null) profile.setTutorPhone(updateDto.tutorPhone());
            if (updateDto.tutorEmail() != null) profile.setTutorEmail(updateDto.tutorEmail());

            studentProfileRepository.save(profile);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCurrentUserData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return Map.of(
                "id",       user.getId(),
                "email",    user.getEmail(),
                "fullName", user.getFullName(),
                "roleId",   user.getRoleId()
        );
    }
}
