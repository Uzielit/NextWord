package com.nextword.backend.feature.user.services;


import com.nextword.backend.feature.user.dto.request.RoleRequest;
import com.nextword.backend.feature.user.dto.request.StudentRegistrationRequest;
import com.nextword.backend.feature.user.dto.request.TeacherRegistrationRequest;
import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentRepository;

    private final TeacherProfileRepository teacherRepository;


    private final PasswordEncoder passwordEncoder;



    public AuthService(
            UserRepository userRepository,
            StudentProfileRepository studentRepository,
            TeacherProfileRepository teacherRepository,
            PasswordEncoder passwordEncoder

    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;

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
