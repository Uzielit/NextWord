package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.payments.repository.PaymentRepository;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.user.dto.request.admin.*;
import com.nextword.backend.feature.user.entity.TeacherProfile;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.TeacherProfileRepository;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;

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

    //pENDIENTE DE PREUGUNTARR
    @Transactional(readOnly = true)
    public List<UserDirectoryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> {
                    String status = "Activo";

                    if (u.getRoleId() == 2) {
                        java.util.Optional<TeacherProfile> profile = teacherRepository.findById(u.getId());
                        if (profile.isPresent() && profile.get().getAccountStatus() != null) {
                            status = profile.get().getAccountStatus();
                        }
                    } else if (u.getRoleId() == 1) {
                        status = "Registrado";
                    } else {
                        status = "Admin";
                    }

                    return new UserDirectoryResponse(
                            u.getId(), u.getFullName(),
                            u.getEmail(), u.getRoleId(), status,
                            u.getRegistrationDate()
                    );
                }).collect(Collectors.toList());
    }

    @Transactional
    public void createTeacher(CreateTeacherRequest req) {
        if (req.email() == null || !req.email().toLowerCase().endsWith("@nextword.com.mx")) {
            throw new RuntimeException("El correo debe pertenecer a la empresa (@nextword.com.mx).");
        }

        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRoleId(2); // Rol Profesor
        User savedUser = userRepository.save(user);

        TeacherProfile profile = new TeacherProfile();
        profile.setId(savedUser.getId());
        profile.setSpecialization("Por definir");
        profile.setYearsOfExperience(0);
        profile.setAccountStatus("Activo");
        teacherRepository.save(profile);
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public void updateProfile(String email, com.nextword.backend.feature.user.dto.request.admin.UpdateProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + email));

        if (req.fullName() != null && !req.fullName().isBlank()) {
            user.setFullName(req.fullName());
        }
        if (req.phoneNumber() != null && !req.phoneNumber().isBlank()) {
            user.setPhoneNumber(req.phoneNumber());
        }
        if (req.profilePicture() != null && !req.profilePicture().isBlank()) {
            user.setProfilePicture(req.profilePicture());
        }
        if (req.newPassword() != null && !req.newPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.newPassword()));
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<ClassHistoryResponse> getClassHistory() {
        // Obtenemos todas las reservas. Asumo que tu ReservationRepository tiene findAll()
        return reservationRepository.findAll().stream()
                .map(reserva -> {
                    // Formateamos la fecha (ajusta según tus campos en Java)
                    String fechaHora = reserva.getSlot().getSlotDate().toString() + " - " + reserva.getSlot().getStartTime();

                    return new ClassHistoryResponse(
                            reserva.getId(),
                            reserva.getTopic() != null ? reserva.getTopic() : "Capacitación General",
                            reserva.getStudent().getFullName(), // Depende de cómo mapeaste la relación
                            reserva.getSlot().getTeacher().getFullName(),
                            fechaHora,
                            reserva.getStatus() // Ej: "Completada", "Cancelada"
                    );
                })
                // Ordenamos para que las más recientes salgan primero
                .sorted((a, b) -> b.datetime().compareTo(a.datetime()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FinancialReportResponse getFinancialReports() {

        ZonedDateTime now = ZonedDateTime.now();
        List<MonthlyIncomeResponse> chartData = new java.util.ArrayList<>();

        // A. Calculamos los últimos 6 meses de forma dinámica
        for (int i = 5; i >= 0; i--) {
            // Obtenemos el mes objetivo (hace 5 meses, hace 4, ..., mes actual)
            ZonedDateTime targetMonth = now.minusMonths(i);

            // Calculamos el primer segundo y el último segundo de ese mes
            ZonedDateTime startOfMonth = targetMonth.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
            ZonedDateTime endOfMonth = targetMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

            // Consultamos a la BD cuánto dinero entró exactamente en ese mes
            BigDecimal monthlySum = paymentRepository.sumIncomeBetweenDates(startOfMonth, endOfMonth);

            // Formateamos el nombre del mes (ej. "Ene", "Feb") con la primera letra mayúscula
            String monthName = ofPattern("MMM", new java.util.Locale("es", "ES")).format(targetMonth);
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);

            chartData.add(new MonthlyIncomeResponse(monthName, monthlySum.doubleValue()));
        }

        // B. Últimas transacciones
        List<TransactionResponse> recentTransactions = paymentRepository.findAll().stream()
                // Ordenamos para ver los pagos más recientes primero
                .sorted((a, b) -> b.getTransactionDate().compareTo(a.getTransactionDate()))
                .limit(10)
                .map(pago -> {
                    // 1. Buscamos el nombre del usuario (PROTEGIDO CONTRA NULL)
                    String studentName = "Usuario Desconocido";
                    if (pago.getUserId() != null && !pago.getUserId().trim().isEmpty()) {
                        studentName = userRepository.findById(pago.getUserId())
                                .map(User::getFullName)
                                .orElse("Usuario Desconocido");
                    }

                    // 2. Buscamos el tema de la clase (PROTEGIDO CONTRA NULL)
                    String topic = "Recarga de Monedero";
                    if (pago.getReservationId() != null && !pago.getReservationId().trim().isEmpty()) {
                        topic = reservationRepository.findById(pago.getReservationId())
                                .map(reserva -> reserva.getTopic() != null ? reserva.getTopic() : "Clase General")
                                .orElse("Clase (Reserva eliminada)");
                    }

                    // 3. Armamos y regresamos el DTO correcto
                    return new TransactionResponse(
                            pago.getId(),
                            topic,
                            studentName,
                            pago.getTransactionDate().toString().substring(0, 10), // Extraemos YYYY-MM-DD
                            pago.getTotalAmount().doubleValue()
                    );
                })
                .collect(Collectors.toList());
        return new FinancialReportResponse(chartData, recentTransactions);
    }
    @Transactional
    public void toggleTeacherStatus(String teacherId, String newStatus) {
        TeacherProfile profile = teacherRepository.findById(teacherId)
                .orElseGet(() -> {
                    TeacherProfile newProfile = new TeacherProfile();
                    newProfile.setId(teacherId);
                    newProfile.setSpecialization("Por definir");
                    newProfile.setYearsOfExperience(0);
                    return newProfile;
                });

        profile.setAccountStatus(newStatus);
        teacherRepository.save(profile);
    }
}
