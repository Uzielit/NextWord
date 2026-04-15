package com.nextword.backend.feature.reservations.services;

import com.nextword.backend.feature.reservations.dto.CancelClassRequest;
import com.nextword.backend.feature.reservations.entity.Cancel;
import com.nextword.backend.feature.reservations.entity.Reservation;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import com.nextword.backend.feature.reservations.repository.CancelRepository;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.reservations.repository.SlotAvailableRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class CancelServices {

    private final CancelRepository cancelRepository;
    private final ReservationRepository reservationRepository;
    private final SlotAvailableRepository slotRepository;
    private final UserRepository userRepository;

    public CancelServices(CancelRepository cancelRepository,
                          ReservationRepository reservationRepository,
                          SlotAvailableRepository slotRepository,
                          UserRepository userRepository) {
        this.cancelRepository = cancelRepository;
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String processCancellation(CancelClassRequest request) {

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"Pagado".equals(reservation.getStatus()) && !"Pendiente".equals(reservation.getStatus())) {
            throw new RuntimeException("Solo puedes cancelar clases pendientes.");
        }

        SlotAvailable slot = reservation.getSlot();
        LocalDate slotDate = slot.getSlotDate();
        LocalTime slotTime = LocalTime.parse(slot.getStartTime());
        LocalDateTime classStartDateTime = LocalDateTime.of(slotDate, slotTime);
        LocalDateTime now = LocalDateTime.now();

        long hoursUntilClass = ChronoUnit.HOURS.between(now, classStartDateTime);

        if (hoursUntilClass < 24) {
            throw new RuntimeException("No se puede cancelar una clase con menos de 24 horas de anticipación");
        }

        reservation.setStatus("Cancelada");
        reservationRepository.save(reservation);

        slot.setStatus("Disponible");
        slotRepository.save(slot);

        User student = reservation.getStudent();
        BigDecimal classPrice = reservation.getMontoPagado();
        student.setWalletBalance(student.getWalletBalance().add(classPrice));
        userRepository.save(student);

        User requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado"));

        Cancel cancelRecord = new Cancel();
        cancelRecord.setReservation(reservation);
        cancelRecord.setRequester(requester);
        cancelRecord.setActionType(request.actionType());
        cancelRecord.setReason(request.reason());
        cancelRecord.setRefundAmount(classPrice);
        cancelRecord.setRequestStatus("APPROVED");

        cancelRepository.save(cancelRecord);

        return "Cancelación exitosa. Se han devuelto $" + classPrice + " a tu monedero virtual.";
    }
}
