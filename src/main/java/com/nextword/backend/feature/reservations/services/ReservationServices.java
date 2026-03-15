package com.nextword.backend.feature.reservations.services;

import com.nextword.backend.feature.reservations.dto.ReservationDto;

import com.nextword.backend.feature.reservations.dto.CompleteClassRequest;
import com.nextword.backend.feature.reservations.dto.ReservationResponseDto;
import com.nextword.backend.feature.reservations.entity.Reservation;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.reservations.repository.SlotAvailableRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServices {
    private final ReservationRepository reservationRepository;
    private final SlotAvailableRepository slotRepository;
    private final UserRepository userRepository;

    public ReservationServices(
            ReservationRepository reservationRepository,
            SlotAvailableRepository slotRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String CreateReservation (ReservationDto request) {
        User student = userRepository.findById(request.studentId())
                .orElseThrow(()-> new RuntimeException("student not found"));

        SlotAvailable slotAvailable= slotRepository.findById(request.slotId())
                .orElseThrow(()-> new RuntimeException("slot not found"));

        if(!"DISPONIBLE".equals(slotAvailable.getStatus())){
            throw new RuntimeException("Reservation status is not DISPONIBLE");
        }

        slotAvailable.setStatus("OCUPADO");
        slotRepository.save(slotAvailable);

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setSlot(slotAvailable);
        Reservation savedReservation = reservationRepository.save(reservation);

        return savedReservation.getId();
    }
    public List<ReservationResponseDto> getStudentReservations(String studentId) {
        return reservationRepository.findByStudentId(studentId)
                .stream()
                .map(res -> new ReservationResponseDto(
                        res.getId(),
                        res.getSlot().getTeacher().getFullName(),
                        res.getSlot().getSlotDate(),
                        res.getSlot().getStartTime(),
                        res.getSlot().getEndTime(),
                        res.getSlot().getClassType(),
                        res.getStatus(),
                        res.getMeetLink() != null ? res.getMeetLink() : "Link pendiente"
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public String completeReservation(CompleteClassRequest request) {

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"PENDING".equals(reservation.getStatus())) {
            throw new RuntimeException("Esta clase ya fue terminada");
        }

        reservation.setStatus("COMPLETED");
        reservation.setStudentAttendance(request.studentAttendance());
        reservation.setTeacherAttendance(request.teacherAttendance());

        reservationRepository.save(reservation);
        return "Clase terminada correctamente";
    }

}
