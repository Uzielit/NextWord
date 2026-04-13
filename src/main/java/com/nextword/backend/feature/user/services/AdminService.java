package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.payments.repository.PaymentRepository;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.user.dto.request.admin.AdminDashboardResponse;
import com.nextword.backend.feature.user.dto.request.admin.CreateTeacherRequest;
import com.nextword.backend.feature.user.dto.request.admin.UserDirectoryResponse;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public AdminService(UserRepository userRepository,
                        TeacherProfileRepository teacherRepository,
                        PasswordEncoder passwordEncoder,
                        ReservationRepository reservationRepository,
                        PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<UserDirectoryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserDirectoryResponse(
                        u.getId(), u.getFullName(), u.getEmail(), u.getRoleId(), "Activo", u.getRegistrationDate()
                )).collect(Collectors.toList());
    }

    public void createTeacher(CreateTeacherRequest req) {
        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRoleId(2); // Rol Profesor
        User savedUser = userRepository.save(user);

        TeacherProfile profile = new TeacherProfile();
        profile.setId(savedUser.getId());
        profile.setSpecialization(req.specialization());
        profile.setYearsOfExperience(req.yearsOfExperience());
        profile.setAccountStatus("Activo");
        teacherRepository.save(profile);
    }
    public AdminDashboardResponse getDashboardStats() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneWeekAgo = now.minus(7, ChronoUnit.DAYS);
        ZonedDateTime firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
        LocalDate today = LocalDate.now();
        int activeProfessors = (int) userRepository.countByRoleId(2);
        int newStudentsThisWeek = (int) userRepository.countByRoleIdAndRegistrationDateAfter(1, oneWeekAgo);
        int classesToday = (int) reservationRepository.countBySlotSlotDate(today);

        int completedClassesThisWeek = (int) reservationRepository.countByStatusAndCreatedAtAfter("Completada", oneWeekAgo);
        int cancelledClassesThisWeek = (int) reservationRepository.countByStatusAndCreatedAtAfter("Cancelada", oneWeekAgo);
        BigDecimal weeklyIncomeBd = paymentRepository.sumIncomeAfterDate(oneWeekAgo);
        BigDecimal monthlyIncomeBd = paymentRepository.sumIncomeAfterDate(firstDayOfMonth);
        double weeklyIncome = weeklyIncomeBd != null ? weeklyIncomeBd.doubleValue() : 0.0;
        double monthlyIncome = monthlyIncomeBd != null ? monthlyIncomeBd.doubleValue() : 0.0;

        return new AdminDashboardResponse(
                activeProfessors,
                classesToday,
                monthlyIncome,
                newStudentsThisWeek,
                completedClassesThisWeek,
                cancelledClassesThisWeek,
                weeklyIncome
        );
    }
    public void updateProfile(String userId, com.nextword.backend.feature.user.dto.request.admin.UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        user.setFullName(req.fullName());
        user.setPhoneNumber(req.phoneNumber());
        user.setProfilePicture(req.profilePicture());

        userRepository.save(user);
    }
}
