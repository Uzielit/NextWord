package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.payments.repository.PaymentRepository;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.user.dto.request.admin.AdminDashboardResponse;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public AdminService(UserRepository userRepository,
                        ReservationRepository reservationRepository,
                        PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    public AdminDashboardResponse getDashboardStats() {

        // 1. Calculamos las fechas que nos interesan
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneWeekAgo = now.minus(7, ChronoUnit.DAYS);
        ZonedDateTime firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
        LocalDate today = LocalDate.now();

        // 2. Extraemos los números de la Base de Datos

        // Profesores activos (Asumiendo Rol 2)
        int activeProfessors = (int) userRepository.countByRoleId(2);

        // Alumnos nuevos (Rol 1) registrados en los últimos 7 días
        int newStudentsThisWeek = (int) userRepository.countByRoleIdAndRegistrationDateAfter(1, oneWeekAgo);

        // Clases de hoy
        int classesToday = (int) reservationRepository.countBySlotSlotDate(today);

        // Clases en la semana (Tus estados pueden variar, ajusta según tu BD: "Completada", "Cancelada")
        int completedClassesThisWeek = (int) reservationRepository.countByStatusAndCreatedAtAfter("Completada", oneWeekAgo);
        int cancelledClassesThisWeek = (int) reservationRepository.countByStatusAndCreatedAtAfter("Cancelada", oneWeekAgo);

        // Ingresos de dinero
        BigDecimal weeklyIncomeBd = paymentRepository.sumIncomeAfterDate(oneWeekAgo);
        BigDecimal monthlyIncomeBd = paymentRepository.sumIncomeAfterDate(firstDayOfMonth);

        // Convertimos a Double para el DTO
        double weeklyIncome = weeklyIncomeBd != null ? weeklyIncomeBd.doubleValue() : 0.0;
        double monthlyIncome = monthlyIncomeBd != null ? monthlyIncomeBd.doubleValue() : 0.0;

        // 3. Armamos el "Paquete" y lo devolvemos
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
}
