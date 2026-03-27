package com.nextword.backend.feature.user.services;


import com.nextword.backend.feature.user.dto.AuthResponseDto;
import com.nextword.backend.feature.user.dto.LoginRequestDto;
import com.nextword.backend.feature.user.dto.request.*;
import com.nextword.backend.feature.user.dto.update.TeacherProfileUpdateDto;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.shared.email.EmailService;
import com.nextword.backend.shared.security.JWTService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentRepository;

    private final TeacherProfileRepository teacherRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailService emailService;



    public AuthService(
            UserRepository userRepository,
            StudentProfileRepository studentRepository,
            TeacherProfileRepository teacherRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JWTService jwtService,
            EmailService emailService

    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;

    }
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Datos incorrectos"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        String token = jwtService.generateToken(request.email());
        return new AuthResponseDto(token);
    }

    @Transactional
    public String registerStudent(StudentRegistrationRequest request) {
        String newUserId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(newUserId);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullname());
        user.setPhoneNumber(request.phoneNumber());
        user.setRoleId(1);
        //Verificacion 2 paso
        String plainCode = String.format("%06d", new Random().nextInt(999999));
        user.setResetToken(passwordEncoder.encode(plainCode));
        user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);


        StudentProfile profile = new StudentProfile();
        profile.setId(newUserId);
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setTutorName(request.tutorName());
        profile.setTutorEmail(request.tutorEmail());
        profile.setTutorPhone(request.tutorPhone());

        studentRepository.save(profile);

        emailService.sendAccountVerificationEmail(user.getEmail(), plainCode);
        return newUserId;
    }


    @Transactional
    public String registerTeacher(TeacherRegistrationRequest request) {


        //Pendiente poner la url de profesores
        String newUserId = UUID.randomUUID().toString();

        User user = new User();
        user.setId(newUserId);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        user.setRoleId(2);
        userRepository.save(user);

        TeacherProfile profile = new TeacherProfile();
        profile.setId(newUserId);
        profile.setSpecialization("Por definir");
        profile.setYearsOfExperience(0);
        profile.setProfessionalDescription("Por definir");
        profile.setCertifications("Ninguna");

        profile.setAccountStatus("ACTIVE");
        profile.setAverageRating(0.0);

        teacherRepository.save(profile);

        return newUserId;
    }
    @Transactional
    public String completeTeacherProfile(String email, TeacherProfileUpdateDto request) {
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

        return "Perfil profesional actualizado exitosamente.";
    }
    @Transactional
    public String forgotPassword(ForgotPasswordRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String plainCode = String.format("%06d", new Random().nextInt(999999));

        user.setResetToken(passwordEncoder.encode(plainCode));
        user.setResetTokenExpiration(java.time.LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), plainCode);

        return "Correo de recuperación enviado con éxito";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getResetTokenExpiration() == null || user.getResetTokenExpiration().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado. Por favor solicita uno nuevo.");
        }
        if (!passwordEncoder.matches(request.token(), user.getResetToken())) {
            throw new RuntimeException("Código de recuperación inválido.");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);

        return "Contraseña actualizada correctamente. Ya puedes iniciar sesión.";
    }

    @Transactional
    public String verifyAccount(VerificationMailRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getResetTokenExpiration() == null || user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado. Por favor, solicita uno nuevo.");
        }


        if (!passwordEncoder.matches(request.code(), user.getResetToken())) {
            throw new RuntimeException("El código de verificación es incorrecto.");
        }

        user.setResetToken(null);
        user.setResetTokenExpiration(null);


        userRepository.save(user);

        return "Cuenta verificada exitosamente. Ya puedes iniciar sesión.";
    }

}
