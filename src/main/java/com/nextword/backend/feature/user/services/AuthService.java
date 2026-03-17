package com.nextword.backend.feature.user.services;


import com.nextword.backend.feature.user.dto.AuthResponseDto;
import com.nextword.backend.feature.user.dto.LoginRequestDto;
import com.nextword.backend.feature.user.dto.request.RoleRequest;
import com.nextword.backend.feature.user.dto.request.StudentRegistrationRequest;
import com.nextword.backend.feature.user.dto.request.TeacherRegistrationRequest;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.shared.security.JWTService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentRepository;

    private final TeacherProfileRepository teacherRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;



    public AuthService(
            UserRepository userRepository,
            StudentProfileRepository studentRepository,
            TeacherProfileRepository teacherRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JWTService jwtService

    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }
    public AuthResponseDto login(LoginRequestDto request) {
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
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        user.setRoleId(1);
        userRepository.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setId(newUserId);
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setTutorName(request.tutorName());
        profile.setTutorContact(request.tutorContact());

        studentRepository.save(profile);

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
        profile.setSpecialization(request.specialization());
        profile.setYearsOfExperience(request.yearsOfExperience());
        profile.setProfessionalDescription(request.professionalDescription());
        profile.setCertifications(request.certifications());
        profile.setHourlyRate(request.hourlyRate());

        profile.setAccountStatus("ACTIVE");
        profile.setAverageRating(0.0);

        teacherRepository.save(profile);

        return newUserId;
    }

}
