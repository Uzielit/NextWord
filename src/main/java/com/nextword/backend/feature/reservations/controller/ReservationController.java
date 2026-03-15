package com.nextword.backend.feature.reservations.controller;

import com.nextword.backend.feature.reservations.dto.*;
import com.nextword.backend.feature.reservations.services.CancelServices;
import com.nextword.backend.feature.reservations.services.ReservationServices;
import com.nextword.backend.feature.reservations.services.SlotAvailableServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final SlotAvailableServices slotAvailableServices;
    private final CancelServices cancelServices;

    private final ReservationServices reservationServices;

    public ReservationController(SlotAvailableServices slotAvailableServices,
                                 ReservationServices reservationServices,
                                 CancelServices cancelServices) {
        this.slotAvailableServices = slotAvailableServices;
        this.reservationServices = reservationServices;
        this.cancelServices = cancelServices;
    }
    @PostMapping("/slot")
    public ResponseEntity<String> createSlot(@RequestBody SlotAvailableDto request) {
        String generatedSlotId = slotAvailableServices.createTeacherSlot(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Slot de horario creado con exito id es " + generatedSlotId);
    }

    @GetMapping("/slots/available")
    public ResponseEntity<List<SlotResponseDto>>getAvailableSlots(){
        List<SlotResponseDto> availableSlots = slotAvailableServices.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(@RequestBody ReservationDto request) {
        String reservationId = reservationServices.CreateReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Reserva hecha , el id es  " + reservationId);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReservationResponseDto>> getStudentReservations(@PathVariable String studentId) {
        List<ReservationResponseDto> myReservations = reservationServices.getStudentReservations(studentId);
        return ResponseEntity.ok(myReservations);
    }
    @PutMapping("/complete")
    public ResponseEntity<String> completeClass(@RequestBody CompleteClassRequest request) {
        String message = reservationServices.completeReservation(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelRequestDto request) {
        String message = cancelServices.processCancellation(request);
        return ResponseEntity.ok(message);
    }
}
