package com.nextword.backend.feature.reservations.services;

import com.nextword.backend.feature.reservations.dto.ReservationDto;

import com.nextword.backend.feature.reservations.dto.CompleteClassRequest;
import com.nextword.backend.feature.reservations.dto.ReservationResponseDto;
import com.nextword.backend.feature.reservations.entity.Reservation;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.reservations.repository.ReviewRepository;
import com.nextword.backend.feature.reservations.repository.SlotAvailableRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServices {
    private final ReservationRepository reservationRepository;
    private final SlotAvailableRepository slotRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReservationServices(
            ReservationRepository reservationRepository,
            SlotAvailableRepository slotRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public String CreateReservation (ReservationDto request) {
        User student = userRepository.findById(request.studentId())
                .orElseThrow(()-> new RuntimeException("student not found"));

        SlotAvailable slotAvailable= slotRepository.findById(request.slotId())
                .orElseThrow(()-> new RuntimeException("slot not found"));

        if(!"Disponible".equals(slotAvailable.getStatus())){
            throw new RuntimeException("Reservation status is not DISPONIBLE");
        }
        BigDecimal classPrice = new BigDecimal("50.00");

        if (student.getWalletBalance() == null || student.getWalletBalance().compareTo(classPrice) < 0) {
            throw new RuntimeException(" Saldo actual: $" +
                    (student.getWalletBalance() == null ? "0.00" : student.getWalletBalance()) +
                    " Costo de la clase " + classPrice);
        }


        student.setWalletBalance(student.getWalletBalance().subtract(classPrice));
        userRepository.save(student);

        slotAvailable.setStatus("OCUPADO");
        slotRepository.save(slotAvailable);

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setSlot(slotAvailable);
        reservation.setMontoPagado(classPrice);
        reservation.setTopic(request.topic());
        Reservation savedReservation = reservationRepository.save(reservation);

        return savedReservation.getId();
    }

    public List<ReservationResponseDto> getTeacherReservations(String teacherId, String status) {
        List<Reservation> reservasDelProfe = reservationRepository.findBySlotTeacherId(teacherId);

        return reservasDelProfe.stream()
                .filter(reserva -> {
                    if (status == null || status.isBlank()) return true;
                    return reserva.getStatus().equalsIgnoreCase(status);
                })
                .map(reserva -> {
                    User estudiante = userRepository.findById(reserva.getStudent().getId())
                            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

                    User profe = userRepository.findById(reserva.getSlot().getTeacher().getId())
                            .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

                    // 🌟 AÑADIDO: Calculamos si ya tiene reseña
                    boolean hasReview = reviewRepository.existsById(reserva.getId());

                    return new ReservationResponseDto(
                            reserva.getId(),
                            estudiante.getFullName(),
                            profe.getFullName(),
                            reserva.getSlot().getSlotDate(),
                            reserva.getSlot().getStartTime(),
                            reserva.getSlot().getEndTime(),
                            reserva.getTopic(),
                            reserva.getStatus(),
                            reserva.getMeetLink(),
                            hasReview // 🌟 Aquí pasamos el valor corregido
                    );
                })
                .toList();
    }
    public List<ReservationResponseDto> getUpcomingAgenda(String userId, boolean isTeacher) {
        LocalDate hoy = LocalDate.now();
        List<Reservation> agenda;

        if (isTeacher) {
            agenda = reservationRepository.findBySlotTeacherIdAndSlotSlotDateGreaterThanEqualOrderBySlotSlotDateAsc(userId, hoy);
        } else {
            agenda = reservationRepository.findByStudentIdAndSlotSlotDateGreaterThanEqualOrderBySlotSlotDateAsc(userId, hoy);
        }

        return agenda.stream()
                .map(res -> {
                    boolean hasReview = reviewRepository.existsById(res.getId());

                    return new ReservationResponseDto(
                            res.getId(),
                            res.getStudent().getFullName(),
                            res.getSlot().getTeacher().getFullName(),
                            res.getSlot().getSlotDate(),
                            res.getSlot().getStartTime(),
                            res.getSlot().getEndTime(),
                            res.getTopic(),
                            res.getStatus(),
                            res.getMeetLink() != null ? res.getMeetLink() : "",
                            hasReview
                    );
                })
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getStudentReservations(String studentId, String status) {
        List<Reservation> reservasDelAlumno = reservationRepository.findByStudentId(studentId);

        return reservasDelAlumno.stream()
                .filter(reserva -> {
                    if (status == null || status.isBlank()) return true;
                    return reserva.getStatus().equalsIgnoreCase(status);
                })
                .map(reserva -> {
                    User estudiante = userRepository.findById(reserva.getStudent().getId())
                            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

                    User profe = userRepository.findById(reserva.getSlot().getTeacher().getId())
                            .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
                    boolean hasReview = reviewRepository.existsById(reserva.getId());

                    return new ReservationResponseDto(
                            reserva.getId(),
                            estudiante.getFullName(),
                            profe.getFullName(),
                            reserva.getSlot().getSlotDate(),
                            reserva.getSlot().getStartTime(),
                            reserva.getSlot().getEndTime(),
                            reserva.getTopic(),
                            reserva.getStatus(),
                            reserva.getMeetLink(),
                            hasReview
                    );
                })
                .toList();
    }

    @Transactional
    public String completeReservation(CompleteClassRequest request) {

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"PendientePago".equals(reservation.getStatus())) {
            throw new RuntimeException("Esta clase ya fue terminada");
        }

        reservation.setStatus("Completada");
        reservation.setStudentAttendance(request.studentAttendance());
        reservation.setTeacherAttendance(request.teacherAttendance());

        reservationRepository.save(reservation);
        return "Clase terminada correctamente";
    }

}
